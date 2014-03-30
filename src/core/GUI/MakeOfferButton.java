package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;


public class MakeOfferButton extends GraphicalObject {
	
	public MakeOfferButton(int x, int y, int width, int height, MarketCanvas canvas) {
		super(x, y, width, height, canvas);
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 20, 20);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(new Color(.5f, .5f, .8f), g);
		
		g.setFont(new Font("Sans Serif", Font.BOLD, 26));
		
		int textx = x + 20;
		int texty = y + 35;
		
		g.setColor(Color.BLACK);
		g.drawString("MAKE OFFER", textx, texty);
	}
	
	@Override
	public void clicked() {
		super.clicked();
		canvas.message("MakeOfferOverlay");
	}
	
}
