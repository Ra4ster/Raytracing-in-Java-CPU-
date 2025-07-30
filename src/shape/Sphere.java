package shape;

import java.awt.Color;

import com.Ray;
import com.Vec3;

public class Sphere extends Shape {
	
	public double r;
	
	public Sphere(double r, Vec3 center, Color color) {
		this.r=r;
		this.center=center;
		this.color=color;
	}
	
	/**
	 * Sets the specular of {@code this}.
	 * 
	 * @param specular An int; 1=matte and 1000=very shiny
	 */
	public void setSpecular(int specular) {
		this.specular=specular;
	}
	
	public void setReflective(double reflective) {
		this.reflective=reflective;
	}
	
	/**
	 * Returns where the ray intersects {@code this}.
	 * 
	 * @param ray Ray shooting at {@code this}
	 * @return location of intersection(s), modeled by {@code P = O + tD} (else {inf,inf})
	 */
	public double[] IntersectRay(Ray ray) {
		Vec3 CO = ray.origin.subtract(center);
		// This is for caching immutable values:
		double dirDotDir = ray.direction.dotProduct(ray.direction);
		double dirDotCO = ray.direction.dotProduct(CO);
		double COdotCO = CO.dotProduct(CO);
		double radiusSquared = r*r;
		
		double a = dirDotDir;
		double b = 2*dirDotCO;
		double c = COdotCO - radiusSquared;
		
		double discriminant = b*b - 4*a*c;
		if (discriminant < 0) return new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
		
		double t1 = (-b + Math.sqrt(discriminant))/(2*a);
		double t2 = (-b - Math.sqrt(discriminant))/(2*a);
		
		if (t1 > t2) { // guarantees closer hit is first
			double temp = t1;
			t1 = t2;
			t2 = temp;
		}
		
		return new double[] { t1, t2 };
	}
	
	@Override
	public String toString() {
		return "(r="+r+", center=" + center.toString() + ", color=" + this.color + ")";
	}

}
