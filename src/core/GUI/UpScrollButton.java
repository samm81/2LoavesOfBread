package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class UpScrollButton extends Button {
	
	Polygon arrow;
	
	public UpScrollButton(int x, int y, int width, int height, Color backgroundColor, Listener listener) {
		super(x, y, width, height, backgroundColor, "", "ScrollUp", listener);
		arrow = new Polygon();
		int pointX = x + 3;
		int pointY = y + height - 3;
		arrow.addPoint(pointX, pointY);
		pointX = x + width - 3;
		arrow.addPoint(pointX, pointY);
		pointX = x + (width / 2);
		pointY = y + 3;
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
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		listener.hear(message, this);
	}
	
}
