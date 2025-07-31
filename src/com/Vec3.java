package com;

public class Vec3 {
	
	public double x,y,z;
	
	public Vec3(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vec3 add(Vec3 other) {
		return new Vec3(this.x+other.x, this.y+other.y, this.z+other.z);
	}
	public Vec3 subtract(Vec3 other) {
		return new Vec3(this.x-other.x, this.y-other.y, this.z-other.z);
	}
	public Vec3 multiply(double val) {
		return new Vec3(this.x*val, this.y*val, this.z*val);
	}
	public double dotProduct(Vec3 other) {
		return this.x*other.x + this.y*other.y + this.z*other.z;
	}
	
	public double length() {
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public Vec3 normalize() {
		double length = this.length();
		
		return new Vec3(x/length, y/length, z/length);
	}
	
	public void set(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public double get(int i) {
		switch (i) {
		case 0:return this.x;
		case 1:return this.y;
		case 2:return this.z;
		default:return Double.NaN;
		}
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
