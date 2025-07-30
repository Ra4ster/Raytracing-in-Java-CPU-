package com;

import java.awt.Color;

import light.AmbientLight;
import light.DirectionalLight;
import light.Light;
import light.PointLight;
import shape.Shape;
import shape.Sphere;

public class Scene {
	
	public Camera cam;
	public Canvas canvas;
	public Shape[] shapes;
	public Light[] lights;
	
	public Scene(Camera cam, Canvas canvas) {
		this.cam=cam;
		this.canvas=canvas;
	}
	
	public void setShapes(Shape[] shapes) {
		this.shapes=shapes;
	}
	
	public void setLights(Light[] lights) {
		this.lights=lights;
	}
	
	/**
	 * Helper method for computing a specular reflection.
	 * 
	 * @param r direction vector of a ray
	 * @param normal Normal (vector)
	 * @return 2*N*dot(N,R) - R
	 */
	public Vec3 reflectRay(Vec3 r, Vec3 normal) {
		return normal.multiply(2)
				.multiply(normal.dotProduct(r))
				.subtract(r);
	}
	
	public Color ComputeLighting(Vec3 point, Vec3 normal, Vec3 v, int specular) {
		Color color = new Color(0,0,0);
		
		for (Light light : lights) {
			if (light instanceof AmbientLight) {
				color = Light.addColors(color, light.adjustedColor());
				continue;
			} else {
				Vec3 L;
				double t_max;
				if (light instanceof PointLight) {
					L = ((PointLight)light).position.subtract(point);
					t_max = 1;
				} else {
					L = ((DirectionalLight)light).direction;
					t_max = Double.POSITIVE_INFINITY;
				}
				if (IntersectsAny(point, L, 0.001, t_max)) continue;
				
				// Diffuse:
				L = L.normalize();
				double n_dot_l = normal.dotProduct(L);
				if (n_dot_l > 0) {
					double tempVal = n_dot_l/(normal.length() * L.length());
					Color temp = Light.intensityTimesColor(light.adjustedColor(), tempVal);
					color = Light.addColors(color, temp);
				}
				// Specular:
				if (specular != -1) {
					Vec3 R = reflectRay(L, normal);
					double r_dot_v = R.dotProduct(v);
					if (r_dot_v > 0) {
						double tempVal = Math.pow(r_dot_v/(R.length()*v.length()), specular);
						Color temp = Light.intensityTimesColor(light.adjustedColor(), tempVal);
						color = Light.addColors(color, temp);
					}
				}
			}
		}
		
		return color;
	}
	
	public Intersection ClosestIntersection(Vec3 O, Vec3 D, double t_min, double t_max) {
		double closest_t = Double.POSITIVE_INFINITY;
		Shape closest_shape = null;
		
		for (Shape shape : shapes) {
			if (shape instanceof Sphere) {
				double[] placesHit = ((Sphere)shape).IntersectRay(new Ray(O, D));
				if (placesHit[0] <= t_max && placesHit[0] >= t_min && placesHit[0] < closest_t) {
					closest_t = placesHit[0];
					closest_shape = shape;
				}
				if (placesHit[1] <= t_max && placesHit[1] >= t_min && placesHit[1] < closest_t) {
					closest_t = placesHit[1];
					closest_shape = shape;
				}
				
			}
		}
		
		return new Intersection(closest_shape, closest_t);
	}
	
	/**
	 * Checks if the ray intersects any shape in the scene
	 * 
	 * This is used for shadow rays: we only care whether a ray is blocked or not.
	 * 
	 * @param O
	 * @param D
	 * @param t_min
	 * @param t_max
	 * @return true if any intersection is found, else false
	 */
	public boolean IntersectsAny(Vec3 O, Vec3 D, double t_min, double t_max) {
		
		for (Shape shape : shapes) {
			if (shape instanceof Sphere) {
				double[] ts = ((Sphere)shape).IntersectRay(new Ray(O, D));
				for (double t : ts) {
					if (t >= t_min && t <= t_max) return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Traces a single ray through all shapes in {@code this}, returning the color of the first-hit shape.
	 * 
	 * @param O origin
	 * @param D direction
	 * @param t_min minimum distance looked
	 * @param t_max maximum distance looked
	 * @param recursion how many times it should be traced (used for mirrors; 0 if not one)
	 * @return closest shape's color
	 */
	public Color TraceRay(Vec3 O, Vec3 D, double t_min, double t_max, int recursion) {
		
		Intersection intersection = ClosestIntersection(O, D, t_min, t_max);
		Shape closest_shape = intersection.shape();
		double closest_t = intersection.t();
		
		if(closest_shape == null) return canvas.BACKGROUND_COLOR;
		
		// Compute local color:
		Vec3 point = O.add(D.multiply(closest_t)); // P = O + tD
		Vec3 normal = point.subtract(closest_shape.center); // N = P - shape.center
		normal = normal.normalize(); // N = N/length(N)
		Color lighting = ComputeLighting(point, normal, D.multiply(-1), closest_shape.specular);
		Color local_color = Light.multiplyColors(closest_shape.color, lighting);
		
		// If we hit the recursion limit or the object isn't reflective, we're done:
		double r = closest_shape.reflective;
		if (recursion <= 0 || r <= 0) return local_color;
		
		// Compute the reflected color:
		Vec3 reflect = reflectRay(D.multiply(-1), normal);
		Color reflected_color = TraceRay(point, reflect, 0.001, Double.POSITIVE_INFINITY, recursion-1); // recursion call
		
		return Light.addColors(Light.intensityTimesColor(local_color, 1-r), Light.intensityTimesColor(reflected_color, r));
	}
}
