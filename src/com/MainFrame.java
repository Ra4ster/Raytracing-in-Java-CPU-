package com;

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
		exitItem.addActionListener(e -> {this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));});
		fileMenu.add(exitItem);
		
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		menuBar.add(viewMenu);
		
		JMenuItem resetItem = new JMenuItem("Reset Camera");
		resetItem.addActionListener(e -> {
			canvasPanel.camera.setOrigin(new Vec3(0,0,0));
			canvasPanel.camera.pitch=0;
			canvasPanel.camera.roll=0;
			canvasPanel.camera.yaw=0;
			canvasPanel.canvas.paintScene(canvasPanel.camera, canvasPanel.scene);
			canvasPanel.repaint();
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
			RotateDialog rotate = new RotateDialog(canvasPanel, new Vec3(canvasPanel.camera.pitch, canvasPanel.camera.yaw, canvasPanel.camera.roll));
			rotate.setVisible(true);
		});
		toolMenu.add(rotateItem);
		
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
