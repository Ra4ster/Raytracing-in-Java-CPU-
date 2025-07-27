package com;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	private Canvas canvas;
	/***/
	private static final long serialVersionUID = 1L;
	
	public void setImage(Canvas canvas) {
		this.canvas=canvas;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(canvas, 0, 0, null);
	}

}
