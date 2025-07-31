package com;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dialogs.MoveDialog;
import dialogs.RotateDialog;

public class MainFrame extends JFrame {
	
	/**/
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private CanvasPanel canvasPanel;
	
	public MainFrame(String str, CanvasPanel canvasPanel) {
		super(str);
		
		this.canvasPanel = canvasPanel;
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);
		
		JMenuItem newItem = new JMenuItem("New Scene");
		fileMenu.add(newItem);
		
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(e -> {this.dispose();});
		fileMenu.add(exitItem);
		
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		menuBar.add(viewMenu);
		
		JMenuItem resetItem = new JMenuItem("Reset Camera");
		resetItem.addActionListener(e -> {
			canvasPanel.reset();
		});
		viewMenu.add(resetItem);
		
		JMenuItem fitItem = new JMenuItem("Fit Item to View");
		fitItem.setEnabled(false); // Items cannot be selected (WIP)
		viewMenu.add(fitItem);
		
		JMenuItem gridItem = new JMenuItem("Toggle Grid");
		gridItem.setEnabled(false); // Items cannot be selected (WIP)
		viewMenu.add(gridItem);
		
		JMenuItem boundingItem = new JMenuItem("Toggle Bounding Boxes");
		boundingItem.setEnabled(false); // Items cannot be selected (WIP)
		viewMenu.add(boundingItem);
		
		JMenu toolMenu = new JMenu("Tools");
		toolMenu.setMnemonic('T');
		menuBar.add(toolMenu);
		
		JMenuItem selectItem = new JMenuItem("Select");
		selectItem.setEnabled(false); // Items cannot be selected (WIP)
		toolMenu.add(selectItem);
		
		JMenuItem moveItem = new JMenuItem("Move Camera");
		moveItem.addActionListener(e -> {
			MoveDialog move = new MoveDialog(canvasPanel, canvasPanel.camera.origin);
			move.setVisible(true);
		});
		toolMenu.add(moveItem); // ROTATE GIZMO
		
		JMenuItem rotateItem = new JMenuItem("Rotate Camera");
		rotateItem.addActionListener(e -> {
			RotateDialog rotate = new RotateDialog(canvasPanel);
			rotate.setVisible(true);
		});
		toolMenu.add(rotateItem);
		
		JMenuItem fpsItem = new JMenuItem("FPS Tracker");
		fpsItem.addActionListener(e -> {
			canvasPanel.setFPSActive(!canvasPanel.fpsActive);
			canvasPanel.repaint();
		});
		toolMenu.add(fpsItem);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_R && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
					canvasPanel.setGizmoMode(GizmoMode.ROTATE);
				} else if (e.getKeyCode() == KeyEvent.VK_M && (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
					canvasPanel.setGizmoMode(GizmoMode.MOVE);
				} else if (e.getKeyCode() == KeyEvent.VK_R && (e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) != 0) {
					canvasPanel.reset();
				} else if (e.getKeyCode() == KeyEvent.VK_R && (e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
					RotateDialog rotate = new RotateDialog(canvasPanel);
					rotate.setVisible(true);
			}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					MainFrame.this.dispose();
				}
			}
		});
		
		JMenu gizmoMenu = new JMenu("Gizmos");
		gizmoMenu.setMnemonic('G');
		menuBar.add(gizmoMenu);
		
		JMenuItem moveGizmoItem = new JMenuItem("Move Gizmo");
		moveGizmoItem.addActionListener(e -> {canvasPanel.setGizmoMode(GizmoMode.MOVE);});
		gizmoMenu.add(moveGizmoItem);
		
		JMenuItem rotateGizmoItem = new JMenuItem("Rotate Camera");
		rotateGizmoItem.addActionListener(e -> {canvasPanel.setGizmoMode(GizmoMode.ROTATE);});
		gizmoMenu.add(rotateGizmoItem);
	}
	public void setCanvasPanel(CanvasPanel canvasPanel) {
		this.canvasPanel=canvasPanel;
	}
}
