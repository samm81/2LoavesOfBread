package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import core.GUI.Button;
import core.GUI.Listener;

/**
 * Button to allow accepting offers
 * @author Sam Maynard
 *
 */
public class AcceptOfferButton extends Button {
	
	/**
	 * constructor
	 */
	public AcceptOfferButton(int x, int y, int width, int height, Color backgroundColor, Listener listener) {
		super(x, y, width, height, backgroundColor, "ACCEPT", "AcceptOffer", listener);
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 10, 25);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(backgroundColor, 1, g);
		
		g.setFont(new Font("Sans Serif", Font.BOLD, 15));
		g.setColor(Color.BLACK);
		
		int textX = x + 9;
		int textY = y + 17;
		
		g.drawString(text, textX, textY);
	}
	
}
