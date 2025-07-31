
package com;
		import java.awt.Color;
		import java.awt.Dimension;
		import java.awt.Graphics2D;
		import java.awt.image.BufferedImage;
		import java.util.ArrayList;
		import java.util.List;
		import java.util.concurrent.ExecutionException;
		import java.util.concurrent.ExecutorService;
		import java.util.concurrent.Executors;
		import java.util.concurrent.Future;
		import java.util.concurrent.TimeUnit;

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
				int newX = this.getWidth()/2 + x;
				int newY = this.getHeight()/2 - y;
				this.setRGB(newX, newY, c.getRGB());
			}
			
			public void putPixel(int x, int y, Color c, int[] buffer) {
				int newX = this.getWidth()/2 + x;
				int newY = this.getHeight()/2 - y;
				buffer[newY*getWidth() + newX] = c.getRGB();
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

				int numThreads = Runtime.getRuntime().availableProcessors(); // Running multiply raytraces parallel
				ExecutorService executor = Executors.newFixedThreadPool(numThreads);
				
				int width = this.getWidth();
				int height = this.getHeight();
				int[] pixels = new int[this.getWidth() * this.getHeight()];
				int stripHeight = height/numThreads;
				
				List<Future<?>> futures = new ArrayList<>();
				
				for (int i=0; i < numThreads; i++) { // parallel computing
					int startY = -height/2 + 1 + i*stripHeight;
					int endY = (i == numThreads - 1) ? height/2 : startY+stripHeight;
					
					futures.add(executor.submit(() -> {
							for (int y=startY; y < endY; y++) {
								for (int x=-width/2 + 1; x < width/2; x++) {
									Vec3 direction = camera.canvasToViewport(this, x, y);
									direction = camera.rotate(direction);
									
									Color color = scene.TraceRay(camera.origin, direction, 0.001, Double.POSITIVE_INFINITY, 2);
									this.putPixel(x, y, color, pixels);
								}
							} 
						}));
					}
				for (Future<?> f : futures) {
				    try {
				        f.get(); // Blocks until that task is done
				    } catch (InterruptedException | ExecutionException e) {
				        e.printStackTrace();
				    }
				}
		
		executor.shutdown();
		try {
			executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setRGB(0, 0, width, height, pixels, 0, width);
		this.panel.repaint();
	}

}
