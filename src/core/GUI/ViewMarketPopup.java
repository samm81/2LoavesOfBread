package core.GUI;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;


public class ViewMarketPopup extends GraphicalObject {
	
	public ViewMarketPopup(int x, int y, int width, int height, DoubleBufferedCanvas canvas) {
		super(x, y, width, height, canvas);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);
	}
	
}
