package com;

public class Camera {
	
	public double pitch=0, yaw=0, roll=0; // x,y,z rotation
	public Vec3 origin;
	private double vW,vH;
	private double d;
	
	public Camera(Vec3 origin) {
		this.origin=origin;
	}
	
	public Camera(Vec3 origin, double yaw, double pitch, double roll) {
		this(origin);
		this.yaw=yaw;
		this.pitch=pitch;
		this.roll=roll;
	}
	
	public void setOrigin(Vec3 origin) {
		this.origin=origin;
	}
	
	public Vec3 rotate(Vec3 dir) {
	    // Apply roll (z-axis)
	    double cosRoll = Math.cos(roll);
	    double sinRoll = Math.sin(roll);
	    double x0 = dir.x * cosRoll - dir.y * sinRoll;
	    double y0 = dir.x * sinRoll + dir.y * cosRoll;
	    double z0 = dir.z;

	    // Apply pitch (x-axis)
	    double cosPitch = Math.cos(pitch);
	    double sinPitch = Math.sin(pitch);
	    double y1 = y0 * cosPitch - z0 * sinPitch;
	    double z1 = y0 * sinPitch + z0 * cosPitch;

	    // Apply yaw (y-axis)
	    double cosYaw = Math.cos(yaw);
	    double sinYaw = Math.sin(yaw);
	    double x2 = x0 * cosYaw + z1 * sinYaw;
	    double z2 = -x0 * sinYaw + z1 * cosYaw;

	    return new Vec3(x2, y1, z2).normalize();
	}
	
	/**
	 * Sets the viewport and distance from the camera.
	 * 
	 * @param vW viewport width
	 * @param vH viewport height
	 * @param d distance from camera
	 */
	public void setViewport(double vW,double vH, double d) {
		this.vW=vW;
		this.vH=vH;
		this.d=d;
	}
	
	/**
	 * Converts canvas coordinates to viewport coordinates
	 * 
	 * @param x Cx
	 * @param y Cy
	 * @return (Vx, Vy, d)
	 */
	public Vec3 canvasToViewport(Canvas canvas, int x, int y) {
		double Vx = x * (vW/canvas.getWidth());
		double Vy = y * (vH/canvas.getHeight());
		
		return new Vec3(Vx,Vy, d);
	}

}
