package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import com.CanvasPanel;
import com.Vec3;

public class RotateDialog extends JDialog {
	
	/***/
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel zLabel;

	public RotateDialog(CanvasPanel canvasPanel) {
		setTitle("Rotate Dialog");
		try {
			setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/assets/Rotate.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(0, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel xPanel = new JPanel();
			xPanel.setBackground(new Color(0, 0, 0));
			contentPanel.add(xPanel);
			{
				JLabel xLabel = new JLabel("X Direction:");
				xLabel.setForeground(Color.WHITE);
				xLabel.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 22));
				xLabel.setBackground(Color.BLACK);
				xPanel.add(xLabel);
			}
			{
				JSpinner xSpinner = new JSpinner();
				xSpinner.setModel(new SpinnerNumberModel(Double.valueOf(canvasPanel.camera.yaw), null, null, Double.valueOf(1)));
				xSpinner.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				xSpinner.setFont(new Font("Verdana", Font.PLAIN, 46));
				xSpinner.setEditor(new JSpinner.NumberEditor(xSpinner, "0.0"));
				xSpinner.setPreferredSize(new Dimension(200, 70));
				xSpinner.addChangeListener((e -> {
					canvasPanel.camera.yaw = (double)xSpinner.getValue()*0.01;
					canvasPanel.canvas.paintScene(canvasPanel.camera, canvasPanel.scene);
					canvasPanel.repaint();
				}));
				xPanel.add(xSpinner);
			}
		}
		{
			JPanel yPanel = new JPanel();
			yPanel.setBackground(new Color(0, 0, 0));
			contentPanel.add(yPanel);
			{
				JLabel yLabel = new JLabel("Y Direction:");
				yLabel.setForeground(Color.WHITE);
				yLabel.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 22));
				yLabel.setBackground(Color.BLACK);
				yPanel.add(yLabel);
			}
			{
				JSpinner ySpinner = new JSpinner();
				ySpinner.setModel(new SpinnerNumberModel(Double.valueOf(canvasPanel.camera.pitch), null, null, Double.valueOf(1)));
				ySpinner.setPreferredSize(new Dimension(200, 70));
				ySpinner.setFont(new Font("Verdana", Font.PLAIN, 46));
				ySpinner.setEditor(new JSpinner.NumberEditor(ySpinner, "0.0"));
				ySpinner.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				ySpinner.addChangeListener((e -> {
					canvasPanel.camera.pitch=(double)ySpinner.getValue()*0.01;
					canvasPanel.canvas.paintScene(canvasPanel.camera, canvasPanel.scene);
					canvasPanel.repaint();
				}));
				yPanel.add(ySpinner);
			}
		}
		{
			JPanel zPanel = new JPanel();
			zPanel.setBackground(new Color(0, 0, 0));
			contentPanel.add(zPanel);
			{
				zLabel = new JLabel("Z Direction:");
				zLabel.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 22));
				zLabel.setForeground(new Color(255, 255, 255));
				zLabel.setBackground(new Color(0, 0, 0));
				zPanel.add(zLabel);
			}
			{
				JSpinner zSpinner = new JSpinner();
				zSpinner.setModel(new SpinnerNumberModel(Double.valueOf(canvasPanel.camera.roll), null, null, Double.valueOf(1)));
				zSpinner.setPreferredSize(new Dimension(200, 70));
				zSpinner.setFont(new Font("Verdana", Font.PLAIN, 46));
				zSpinner.setEditor(new JSpinner.NumberEditor(zSpinner, "0.0"));
				zSpinner.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
				zSpinner.addChangeListener(e -> {
					canvasPanel.camera.roll=(double)zSpinner.getValue()*0.01;
					canvasPanel.canvas.paintScene(canvasPanel.camera, canvasPanel.scene);
					canvasPanel.repaint();
					
				});
				zPanel.add(zSpinner);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(0, 0, 0));
			buttonPane.setAlignmentY(Component.TOP_ALIGNMENT);
			buttonPane.setAlignmentX(Component.LEFT_ALIGNMENT);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(150, 23));
				okButton.setMaximumSize(new Dimension(200, 23));
				okButton.setMinimumSize(new Dimension(100, 23));
				okButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
				okButton.setHorizontalTextPosition(SwingConstants.CENTER);
				okButton.setActionCommand("OK");
				okButton.addActionListener(e -> {this.dispose();});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
