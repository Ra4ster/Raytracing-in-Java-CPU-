package light;

import java.awt.Color;

public abstract class Light {
	
	public Color color;
	public double intensity;
	
	public Color adjustedColor() {
		int r = (int) Math.min(color.getRed()*intensity, 255.0);
		int g = (int) Math.min(color.getGreen()*intensity, 255.0);
		int b = (int) Math.min(color.getBlue()*intensity, 255.0);
		
		return new Color(r,g,b);
	}
	
	public static Color addColors(Color c1, Color c2) {
		// Start with color 1:
		int r = c1.getRed();
		int g = c1.getGreen();
		int b = c1.getBlue();
		// Add color 2:
		r += c2.getRed();
		g += c2.getGreen();
		b += c2.getBlue();
		// Bound between 0 and 255:
		r=Math.min(r, 255);
		g=Math.min(g, 255);
		b=Math.min(b, 255);
		
		return new Color(r, g, b);
	}
	
	public static Color intensityTimesColor(Color c, double intensity) {
		return new Color((int)Math.min(c.getRed()*intensity,255), (int)Math.min(c.getGreen()*intensity,255), (int)Math.min(c.getBlue()*intensity,255));
	}
	
	public static Color multiplyColors(Color c1, Color c2) {
		// Start with color 1:
		int r = c1.getRed();
		int g = c1.getGreen();
		int b = c1.getBlue();
		// Multiply color 2:
		r *= c2.getRed();
		g *= c2.getGreen();
		b *= c2.getBlue();
		// Bound between 0 and 255:
		r=Math.min(r/255, 255);
		g=Math.min(g/255, 255);
		b=Math.min(b/255, 255);
		
		return new Color(r, g, b);
	}
}
