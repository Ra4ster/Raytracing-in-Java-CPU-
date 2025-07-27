package light;

import java.awt.Color;

import com.Vec3;

/**
 * Light that shines everywhere in one direction.
 */
public class DirectionalLight extends Light {
	
	public Vec3 direction;
	
	public DirectionalLight(Vec3 direction, double intensity, Color color) {
		this.direction=direction;
		this.intensity=intensity;
		this.color=color;
	}
}
