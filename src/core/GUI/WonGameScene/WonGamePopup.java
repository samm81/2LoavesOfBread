package core.GUI.WonGameScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import core.GUI.GraphicalObject;


public class WonGamePopup extends GraphicalObject {
	
	public WonGamePopup(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(g);
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 36));
		g.setColor(Color.BLACK);
		g.drawString("Congratulations!!", x + 50, y + height - 40);
	}
	
}
