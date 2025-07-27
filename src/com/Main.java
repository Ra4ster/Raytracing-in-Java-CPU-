package com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		Sphere sphere2 = new Sphere(1, new Vec3(2, 0, 4), Color.BLUE);
		sphere2.setSpecular(500);
		Sphere sphere3 = new Sphere(1, new Vec3(-2, 0, 4), Color.GREEN);
		sphere3.setSpecular(10);
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
		int width = 800, height = 800;
		
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Raytracer");
			try {
				frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/assets/Picture.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			CanvasPanel canvasPanel = new CanvasPanel();
			
			Canvas canvas = new Canvas(width, height, Color.BLACK); // black background
			
			canvas.setPanel(canvasPanel);
			
			canvas.fill(Color.BLACK);
			
			Camera camera = new Camera(new Vec3(0,0,0));
			
			
			Scene scene = new Scene(camera, canvas);
			
			loadAll(scene, canvas, camera);
			
			frame.getContentPane().add(canvasPanel, BorderLayout.CENTER);
			
			JPanel CameraX = new JPanel();
			frame.getContentPane().add(CameraX, BorderLayout.SOUTH);
			CameraX.setLayout(new BoxLayout(CameraX, BoxLayout.X_AXIS));
			
			JLabel xLabel = new JLabel("X");
			xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			xLabel.setMaximumSize(new Dimension(30, 14));
			xLabel.setMinimumSize(new Dimension(30, 14));
			xLabel.setPreferredSize(new Dimension(30, 14));
			CameraX.add(xLabel);
			
			JSlider xSlider = new JSlider(-10,10);
			xSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Vec3 newOrigin = camera.origin;
					newOrigin.x = xSlider.getValue();
					camera.setOrigin(newOrigin);
					canvas.paintScene(camera, scene);
					frame.repaint();
				}
			});
			CameraX.add(xSlider);
			
			JPanel CameraY = new JPanel();
			CameraY.setLayout(new BoxLayout(CameraY, BoxLayout.Y_AXIS));
			
			JLabel yLabel = new JLabel("Y");
			yLabel.setMinimumSize(new Dimension(30, 14));
			
			CameraY.add(yLabel);
			
			frame.getContentPane().add(CameraY, BorderLayout.EAST);
			
			JSlider ySlider = new JSlider(-10,10);
			ySlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Vec3 newOrigin = camera.origin;
					newOrigin.y = ySlider.getValue();
					camera.setOrigin(newOrigin);
					canvas.paintScene(camera, scene);
					frame.repaint();
				}
			});
			ySlider.setOrientation(SwingConstants.VERTICAL);
			CameraY.add(ySlider);
			
			JPanel CameraZ = new JPanel();
			frame.getContentPane().add(CameraZ, BorderLayout.WEST);
			CameraZ.setLayout(new BoxLayout(CameraZ, BoxLayout.Y_AXIS));
			
			JSlider zSlider = new JSlider(-10,10);
			zSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					Vec3 newOrigin = camera.origin;
					newOrigin.z = zSlider.getValue();
					camera.setOrigin(newOrigin);
					canvas.paintScene(camera, scene);
					frame.repaint();
				}
			});
			zSlider.setOrientation(SwingConstants.VERTICAL);
			CameraZ.add(zSlider);
			
			JLabel zLabel = new JLabel("Z");
			CameraZ.add(zLabel);
			
			
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
			frame.repaint();	
		});
	}
}
