package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import light.AmbientLight;
import light.DirectionalLight;
import light.Light;
import light.PointLight;
import shape.Shape;
import shape.Sphere;

public class Main {
	
	public static Shape[] spheres() {
		Sphere sphere1 = new Sphere(1, new Vec3(0, -1, 3), Color.RED);
		sphere1.setSpecular(500);
		sphere1.setReflective(0.5);
		Sphere sphere2 = new Sphere(1, new Vec3(2, 0, 4), Color.BLUE);
		sphere2.setSpecular(500);
		sphere2.setReflective(0);
		Sphere sphere3 = new Sphere(1, new Vec3(-2, 0, 4), Color.GREEN);
		sphere3.setSpecular(10);
		sphere3.setReflective(0);
		Sphere sphereFloor = new Sphere(5000, new Vec3(0, -5001, 0), Color.YELLOW);
		sphereFloor.setSpecular(1000);
		
		return new Shape[] {sphere1,sphere2,sphere3,sphereFloor};
	}
	
	public static Light[] lights() {
		Light light1 = new AmbientLight(0.2, Color.WHITE);
		Light light2 = new PointLight(new Vec3(2, 1, 0), 0.6, Color.WHITE);
		Light light3 = new DirectionalLight(new Vec3(1, 4, 4), 0.2, Color.WHITE);
		
		return new Light[] {light1, light2, light3};
	}
	
	public static void loadAll(Scene scene, Canvas canvas, Camera camera) {
		scene.setShapes(spheres());
		scene.setLights(lights());
		camera.setViewport(1, 1, 1);
		canvas.paintScene(camera, scene);
	}
	
	public static void main(String[] args) {
		int width = 400, height = 300;
		
		SwingUtilities.invokeLater(() -> {
			CanvasPanel canvasPanel = new CanvasPanel();
			MainFrame frame = new MainFrame("Raytracer", canvasPanel);
			try {
				frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/assets/Picture.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Canvas canvas = new Canvas(width, height, Color.BLACK); // black background
			Camera camera = new Camera(new Vec3(0,0,0));
			Scene scene = new Scene(camera, canvas);
			
			canvasPanel.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					int newWidth = canvasPanel.getWidth();
					int newHeight = canvasPanel.getHeight();
					
					if (newWidth > 0 && newHeight > 0) {
						Canvas newCanvas = new Canvas(newWidth, newHeight, Color.BLACK);
						newCanvas.setPanel(canvasPanel);
						canvasPanel.attach(camera, scene, newCanvas);
						
						double aspectRatio = (double)newWidth / newHeight;
						camera.setViewport(aspectRatio, 1, 1);
						
						scene.canvas = newCanvas;
						newCanvas.paintScene(camera, scene);
						canvasPanel.repaint();
					}
				}
			});
			canvasPanel.addMouseWheelListener(e -> {
			    int notches = e.getWheelRotation(); // negative = zoom in, positive = zoom out
			    
			    double zoomSpeed = 0.5; // adjust speed of zoom
			    double newZ = camera.origin.z + notches * zoomSpeed;
			    
			    camera.setOrigin(new Vec3(camera.origin.x, camera.origin.y, newZ));
			    
			    // Repaint scene with new camera position
			    scene.canvas.paintScene(camera, scene);
			    canvasPanel.repaint();
			});
			
			canvas.setPanel(canvasPanel);
			canvasPanel.attach(camera, scene, canvas);
			canvasPanel.setGizmoMode(GizmoMode.MOVE);
			canvas.fill(Color.BLACK);
			
			
			loadAll(scene, canvas, camera);
			
			frame.getContentPane().add(canvasPanel, BorderLayout.CENTER);
			
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			
			new javax.swing.Timer(16, e -> {
				scene.canvas.paintScene(camera, scene);
				canvasPanel.repaint();
			}).start();
		});
	}
}
