package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class to hold generally useful GUI utilities.
 * 
 * @author Sam "Fabulous Hands" Maynard
 *
 */
public class GUIUtils {
	
	/**
	 * Draws a rounded rectangle with a white inside and black border at
	 * the given x and y, with the given width and height, and the given
	 * rounding of the rectangles.
	 * 
	 * @param x x coord to draw the outline at
	 * @param y y coord to draw the outline at
	 * @param width width of the outline
	 * @param height height of the outline
	 * @param rounding rounding of rectangles to draw the outline with
	 * @param g Graphics2D object to do the drawing with
	 */
	public static void drawOutline(int x, int y, int width, int height, int rounding, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, rounding, rounding);
		int offset = 3;
		g.setColor(Color.WHITE);
		// g.fillRoundRect(x + offset, y + offset, width - offset, height - offset, 50, 50); // cool effect
		g.fillRoundRect(x + offset, y + offset, width - offset * 2, height - offset * 2, rounding, rounding);
	}
	
}
