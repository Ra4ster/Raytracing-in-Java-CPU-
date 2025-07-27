package com;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Canvas extends BufferedImage {
	
	private CanvasPanel panel;
	
	public Color BACKGROUND_COLOR;
	
	/**
	 * Creates a canvas with a centered coordinate system, though input width and height are still screen-value.
	 * 
	 * @param width width of screen
	 * @param height height of screen
	 * @param imageType specified by BufferedImage
	 */
	public Canvas(int width, int height, Color background) {
		super(width, height, BufferedImage.TYPE_INT_RGB);
		this.BACKGROUND_COLOR = background;
	}
	
	public void setPanel(CanvasPanel panel) {
		panel.setImage(this);
		panel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		
		this.panel=panel;
	}

	/**
	 * Puts a color pixel onto the (x,y)th position, using a centered coordinate system.
	 * 
	 * @param x x-value
	 * @param y y-value
	 * @param c Color
	 */
	public void putPixel(int x, int y, Color c) {
		int[] realLoc = locToCoordinate(x,y);
		
		this.setRGB(realLoc[0], realLoc[1], c.getRGB());
	}
	
	public void fill(Color color) {
		Graphics2D g2d = this.createGraphics();
		
		g2d.setColor(color);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.dispose();
		
		
		this.BACKGROUND_COLOR=color;
		this.panel.repaint();
	}
	
	/**
	 * Converts screen location into coordinates. IE (0,0) is actually (width/2, height/2).
	 * 
	 * @param x x-coord
	 * @param y y-coord
	 * @return actual pixel location {x, y}
	 */
	private int[] locToCoordinate(int x, int y) {
		// S_x = C_w/2 + C_x
		// S_y = C_h/2 - C_y (reverses y-direction)
		
		int newX = this.getWidth()/2 + x;
		int newY = this.getHeight()/2 - y;
		
		return new int[] {newX, newY};
	}
	
	/**
	 * Paints all shapes in the scene using the camera as the origin.
	 * 
	 * @param camera Camera
	 * @param scene Grouping of shapes and lights
	 */
	public void paintScene(Camera camera, Scene scene) {
		for (int y=-this.getHeight()/2+1; y < this.getHeight()/2; y++) {
			for (int x=-this.getWidth()/2+1; x < this.getWidth()/2; x++) {
				Vec3 distance = camera.canvasToViewport(this, x, y);
				
				Color color = scene.TraceRay(camera, new Ray(camera.origin, distance), 1, Double.MAX_VALUE);
				this.putPixel(x, y, color);
			}
		}
	}

}
