package light;

import java.awt.Color;

/**
 * Light that fills the entire space.
 */
public class AmbientLight extends Light {
	
	public AmbientLight(double intensity, Color color) {
		this.intensity=intensity;
		this.color=color;
	}
}
