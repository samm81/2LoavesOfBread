package core.GUI.CommodityCrashScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import core.GUI.GraphicalObject;
import core.commodities.Commodity;

/**
 * A message to inform the user that a commodity has
 * left the market
 * @author Sam Maynard
 *
 */
public class CommodityCrashPopup extends GraphicalObject {
	
	Commodity commodity;
	
	/**
	 * constructor
	 * @param commodity commodity that has disappeared from the market
	 */
	public CommodityCrashPopup(int x, int y, int width, int height, Commodity commodity) {
		super(x, y, width, height);
		this.commodity = commodity;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(g);
		
		Color color = commodity.getColor();
		String name = commodity.name();
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 36));

		g.setColor(color);
		g.drawString(name, x + 20, y + height - 40);
		g.setColor(Color.BLACK);
		g.drawString("has disappeared from the market", x + 250, y + height - 40);
	}
	
}
