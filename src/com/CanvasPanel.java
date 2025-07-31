package com;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	/***/
	private static final long serialVersionUID = 1L;
	
	private Vec3 dragStart = null; // used for gizmo dragging
	private boolean hoveringGizmo = false; // hovering near gizmo
	private char activeAxis = 'N'; // Active axis hovering currently (none by default)
	private GizmoMode gizmo = GizmoMode.MOVE;
	public Canvas canvas;
	public Camera camera;
	public Scene scene;
	public BufferedImage image;
	
	private long lastFpsTime = System.nanoTime();
	private int fps, framesThisSecond;
	private Rectangle gizmoBounds = new Rectangle();
	public boolean fpsActive = false;
	
	public void setImage(BufferedImage image) {
		this.image=image;
	}
	
	public void setGizmoMode(GizmoMode gizmo) {
		this.gizmo = gizmo;
		// Trigger hover check again
		java.awt.Point mouse = getMousePosition();
		if (mouse != null) {
			boolean nowHovering = gizmoBounds.contains(mouse);
			if (nowHovering != hoveringGizmo) {
				hoveringGizmo = nowHovering;
			}
		}
		repaint();
	}
	
	public void attach(Camera camera, Scene scene, Canvas canvas) {
		this.camera=camera;
		this.scene=scene;
		this.canvas=canvas;
	}
	
	public CanvasPanel() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dragStart = new Vec3(e.getX(), e.getY(), 0);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dragStart=null;
				activeAxis='N';
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragStart == null || camera == null) return;
				
				Vec3 dragNow = new Vec3(e.getX(), e.getY(), 0);
				Vec3 delta = dragNow.subtract(dragStart); // change in dragging
				Vec3 dragVec = delta.normalize();
				
				// Unit vectors for gizmo axis:
				double xDot = dragVec.dotProduct(new Vec3(1, 0, 0));
				double yDot = dragVec.dotProduct(new Vec3(0, 1, 0));
				double zDot = dragVec.dotProduct(new Vec3(-0.707, 0.707, 0));
				if (activeAxis == 'N') {
					double absX = Math.abs(xDot);
					double absY = Math.abs(yDot);
					double absZ = Math.abs(zDot);
					
					double threshold = 0.85;
					
					if (absX > threshold && absX > absY && absX > absZ) {
						// dragged in x direction
						activeAxis = 'X';
					} else if (absY > threshold && absY > absX && absY > absZ) {
						// dragged in y direction
						activeAxis = 'Y';
					} else if (absZ > 0.6 && absZ > absX && absZ > absY) {
						// dragged bottom left / top right
						activeAxis = 'Z';
					}
				}
				Vec3 camPos = camera.origin;
				if (gizmo == GizmoMode.MOVE) {
					switch (activeAxis) {
					case 'X':
						camPos.x += delta.x*0.01;
						break;
					case 'Y':
						camPos.y -= delta.y*0.01;
						break;
					case 'Z':
						camPos.z -= delta.y*0.01;
						break;
					default: break;
					}
					camera.setOrigin(camPos);
				} else if (gizmo == GizmoMode.ROTATE) {
					switch (activeAxis) {
					case 'X':
						camera.yaw += delta.x*0.01;
						break;
					case 'Y':
						camera.pitch -= delta.y*0.01;
						break;
					case 'Z':
						camera.roll -= delta.y*0.01;
						break;
					default: break;
					}
				}
				
				dragStart = dragNow;
				canvas.paintScene(camera, scene);
				repaint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				boolean nowHovering = gizmoBounds.contains(e.getPoint());
				if (nowHovering != hoveringGizmo) {
					hoveringGizmo=nowHovering;
					repaint();
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// FPS
		long now  = System.nanoTime();
		if (now - lastFpsTime >= 1_000_000_000L) { // Per second
			fps = framesThisSecond;
			framesThisSecond=0; // Count frames
			lastFpsTime=now;
		}
		
		if (image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
		Graphics2D g2d = (Graphics2D)g;
		if (gizmo == GizmoMode.ROTATE) {
			drawRotateGizmo(g2d);
		} else {
			drawMoveGizmo(g2d);
		}
		if (fpsActive) {

			framesThisSecond++;
			g2d.setColor(Color.YELLOW);
			g2d.drawString("FPS: " + fps, 10, 20);
		}
	}
	
	/**
	 * Draws a movement gizmo in the top right, used for moving the camera
	 * @param g2 Graphics
	 */
	public void drawMoveGizmo(Graphics2D g2) {
		int size = 60;
		int centerX = getWidth() - size; // 60 to the left from the edge of the panel
		int centerY = size; // 60 down
		int axisLength = 30;
		
		gizmoBounds.setBounds(centerX-12, centerY-12, 24, 24);
		
		if (hoveringGizmo) {
			g2.setColor(new Color(255, 255, 255, 50));
			g2.fillOval(centerX-12, centerY-12, 24, 24);
		}
		
		g2.setColor(Color.GRAY);
		g2.drawOval(centerX-4, centerY-4, 8, 8);
		
		// X axis
		g2.setColor(activeAxis == 'X' ? Color.PINK : Color.RED);
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(centerX, centerY, centerX+axisLength, centerY);
		g2.drawString("X", centerX+axisLength+4, centerY);
		
		// Y axis
		g2.setColor(activeAxis == 'Y' ? new Color(0, 180, 60) : new Color(0, 100, 0));
		g2.drawLine(centerX, centerY, centerX, centerY-axisLength);
		g2.drawString("Y", centerX-5, centerY-axisLength-4);
		
		// Z
		g2.setColor(activeAxis == 'Z' ? new Color(30, 144, 255) : new Color(0, 102, 204));
		g2.drawLine(centerX, centerY, centerX-axisLength/2, centerY+axisLength/2);
		g2.drawString("Z", centerX - axisLength/2 - 10, centerY + axisLength/2 + 4);
	}
	
	public void drawRotateGizmo(Graphics2D g2) {
		int size = 60;
		int centerX = getWidth() - size; // 60 to the left from the edge of the panel
		int centerY = size; // 60 down
		int axisLength = 30;
		
		gizmoBounds.setBounds(centerX-12, centerY-12, 24, 24);
		if (hoveringGizmo) {
			g2.setColor(new Color(255, 255, 255, 50));
			g2.fillOval(centerX-12, centerY-12, 24, 24);
		}
		g2.setColor(Color.GRAY);
		g2.drawOval(centerX-4, centerY-4, 8, 8);
		
	    g2.setColor(activeAxis == 'X' ? Color.PINK : Color.RED);
	    g2.drawOval(centerX - axisLength, centerY - 5, axisLength * 2, 10);
	    g2.drawString("X", centerX + axisLength + 5, centerY + 5);
	    
	    g2.setColor(activeAxis == 'Y' ? new Color(0, 180, 60) : new Color(0, 100, 0));
	    g2.drawOval(centerX-5, centerY-axisLength, 10, axisLength*2);
	    g2.drawString("Y", centerX - 5, centerY - axisLength - 5);
	    
	    g2.setColor(activeAxis == 'Z' ? new Color(30, 144, 255) : new Color(0, 102, 204));
	    g2.drawOval(centerX - axisLength, centerY-axisLength, axisLength*2, axisLength*2);
	    g2.drawString("Z", centerX - axisLength - 3, centerY + axisLength + 3);
	}
	
	public void setFPSActive(boolean val) {
		fpsActive=val;
	}
	
	public void reset() {
		camera.setOrigin(new Vec3(0,0,0));
		camera.pitch=0;
		camera.roll=0;
		camera.yaw=0;
		canvas.paintScene(camera, scene);
		repaint();
	}
}
