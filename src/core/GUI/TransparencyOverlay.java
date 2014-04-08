package core.GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class TransparencyOverlay extends GraphicalObject {
	
	Color color;
	
	public TransparencyOverlay(DoubleBufferedCanvas canvas, Color color) {
		super(0, 0, canvas.getWidth(), canvas.getHeight(), canvas);
		this.color = color;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle2D.Float(x, y, width, height);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		g.setColor(color);
		g.fill(shape);
	}
	
	@Override
	public void clicked(MouseEvent e) {
		super.clicked(e);
		canvas.message("CloseMakeOffer");
	}
	
}
