package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;

import core.Offer;
import core.channels.OfferChannel;
import core.commodities.Commodity;

public class ViewMarketPopup extends GraphicalObject {
	
	OfferChannel offerChannel;
	
	public ViewMarketPopup(int x, int y, int width, int height, OfferChannel offerChannel, DoubleBufferedCanvas canvas) {
		super(x, y, width, height, canvas);
		this.offerChannel = offerChannel;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 25));
		
		Collection<Offer> offers = offerChannel.getOffersMap().values();
		
		int offerX = this.x + 40;
		int offerY = this.y + 60;
		
		for(Offer offer : offers) {
			if(offer != null) {
				int volume1 = offer.getMaxTradeVolume();
				int volume2 = offer.getMinReceive();
				Commodity commodity1 = offer.getCommodity1();
				Commodity commodity2 = offer.getCommodity2();
				
				drawNum(volume1, offerX, offerY, g);
				offerX += 40;
				drawCommodity(commodity1, offerX, offerY, g);
				offerX += 170;
				drawString(" for ", offerX, offerY, g);
				offerX += 70;
				drawNum(volume2, offerX, offerY, g);
				offerX += 40;
				drawCommodity(commodity2, offerX, offerY, g);
				
				offerX = this.x + 40;
				offerY += 40;
			}
		}
	}

	private void drawNum(int num, int x, int y, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Sans Serif", Font.BOLD, 26));
		g.drawString("" + num, x, y);
	}
	
	private void drawCommodity(Commodity commodity, int x, int y, Graphics2D g) {
		g.setColor(commodity.getColor());
		g.setFont(new Font("Sans Serif", Font.PLAIN, 25));
		g.drawString(commodity.name(), x, y);
	}
	
	private void drawString(String string, int x, int y, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Sans Serif", Font.PLAIN, 26));
		g.drawString(string, x, y);
	}
	
}
