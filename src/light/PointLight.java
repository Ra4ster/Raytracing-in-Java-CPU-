package light;

import java.awt.Color;

import com.Vec3;

/**
 * Light that shines from a single point.
 */
public class PointLight extends Light {
	
	public Vec3 position;
	
	public PointLight(Vec3 position, double intensity, Color color) {
		this.position=position;
		this.intensity=intensity;
		this.color=color;
	}
}
