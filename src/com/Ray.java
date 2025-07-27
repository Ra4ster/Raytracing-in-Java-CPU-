package com;

/**
 * Line of infinite length, going from an origin in a specific direction
 */
public class Ray {
	
	public Vec3 origin, direction;

	public Ray(Vec3 origin, Vec3 direction) {
		this.origin=origin;
		this.direction=direction;
	}
	
	/**
	 * Moving along the ray, modeled by equation {@code P=O+tD}.
	 * @param t amount to move along
	 * @return new point
	 */
	public Vec3 move(double t) {
		return direction.multiply(t).add(origin);
	}
}
