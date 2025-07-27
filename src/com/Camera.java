package com;

public class Camera {
	
	public Vec3 origin;
	private int vW,vH;
	private int d;
	
	public Camera(Vec3 origin) {
		this.origin=origin;
	}
	
	public void setOrigin(Vec3 origin) {
		this.origin=origin;
	}
	
	/**
	 * Sets the viewport and distance from the camera.
	 * 
	 * @param vW viewport width
	 * @param vH viewport height
	 * @param d distance from camera
	 */
	public void setViewport(int vW,int vH, int d) {
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
		double Vx = x * ((double)vW/canvas.getWidth());
		double Vy = y * ((double)vH/canvas.getHeight());
		
		return new Vec3(Vx,Vy, d);
	}

}
