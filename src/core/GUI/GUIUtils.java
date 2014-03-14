package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;


public class GUIUtils {
	
	public static void drawOutline(int x, int y, int width, int height, int rounding, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, rounding, rounding);
		int offset = 3;
		g.setColor(Color.WHITE);
		// g.fillRoundRect(x + offset, y + offset, width - offset, height - offset, 50, 50); // cool effect
		g.fillRoundRect(x + offset, y + offset, width - offset * 2, height - offset * 2, rounding, rounding);
	}
	
}
