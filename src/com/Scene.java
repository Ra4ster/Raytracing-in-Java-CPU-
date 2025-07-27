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
				Intersection intersection = ClosestIntersection(new Ray(point, L), 0.001, t_max);
				if (intersection.shape() != null) continue;
				
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
					Vec3 R = normal.multiply(2).multiply(normal.dotProduct(L)).subtract(L);
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
	
	public Intersection ClosestIntersection(Ray ray, double t_min, double t_max) {
		double closest_t = Double.POSITIVE_INFINITY;
		Shape closest_shape = null;
		
		for (Shape shape : shapes) {
			if (shape instanceof Sphere) {
				double[] placesHit = ((Sphere)shape).IntersectRay(ray);
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
	 * Traces a single ray through all shapes in {@code this}, returning the color of the first-hit shape.
	 * 
	 * @param camera Camera
	 * @param ray Ray
	 * @param t_min minimum distance looked
	 * @param t_max maximum distance looked
	 * @return closest shape's color
	 */
	public Color TraceRay(Camera camera, Ray ray, double t_min, double t_max) {
		
		Intersection intersection = ClosestIntersection(ray, t_min, t_max);
		Shape closest_shape = intersection.shape();
		double closest_t = intersection.t();
		
		if(closest_shape == null) return canvas.BACKGROUND_COLOR;
		
		Vec3 point = camera.origin.add(ray.direction.multiply(closest_t)); // P = O + tD
		Vec3 normal = point.subtract(closest_shape.center); // N = P - shape.center
		normal = normal.normalize(); // N = N/length(N)
		
		Color lighting = ComputeLighting(point, normal, ray.direction.multiply(-1), closest_shape.specular);
		return Light.multiplyColors(closest_shape.color, lighting);
	}
}
