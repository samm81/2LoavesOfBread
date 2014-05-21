package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * Button to scroll down
 * @author Sam Maynard
 *
 */
public class DownScrollButton extends Button {
	
	Polygon arrow;
	
	/**
	 * See Button
	 */
	public DownScrollButton(int x, int y, int width, int height, Color backgroundColor, Listener listener) {
		super(x, y, width, height, backgroundColor, "", "ScrollDown", listener);
		arrow = new Polygon();
		
		int size = width - 16;
		
		int pointX = x + (width - size) / 2;
		int pointY = y + (height / 2) - (size / 2);
		arrow.addPoint(pointX, pointY);
		pointX += size;
		arrow.addPoint(pointX, pointY);
		pointX = x + (width / 2);
		pointY += size;
		arrow.addPoint(pointX, pointY);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 15, 15);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(backgroundColor, g);
		
		g.setColor(Color.BLACK);
		g.fill(arrow);
	}
	
}
