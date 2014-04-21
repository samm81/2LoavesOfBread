package core.GUI;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;

import core.Offer;
import core.channels.OfferChannel;

public class ViewMarketPopup extends GraphicalObject {
	
	OfferChannel offerChannel;
	
	public ViewMarketPopup(int x, int y, int width, int height, OfferChannel offerChannel) {
		super(x, y, width, height);
		this.offerChannel = offerChannel;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);
		
		Collection<Offer> offers = offerChannel.getOffersMap().values();
		
		int offerX = this.x + 20;
		int offerY = this.y + 20;
		
		for(Offer offer : offers) {
			if(offer != null) {
				OfferEntry offerEntry = new OfferEntry(offerX, offerY, 700, 30, offer);
				offerEntry.drawSelf(g);
				
				offerY += 40;
			}
		}
	}
	
}
