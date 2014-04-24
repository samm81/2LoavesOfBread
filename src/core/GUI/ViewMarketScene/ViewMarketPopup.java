package core.GUI.ViewMarketScene;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import core.Offer;
import core.GUI.GraphicalObject;
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
		Comparator<Offer> comparator = new Comparator<Offer>() {
			public int compare(Offer offer1, Offer offer2) {
				int compare = offer1.getCommodity1().compareTo(offer2.getCommodity1());
				if(compare == 0) {
					compare = offer1.getCommodity2().compareTo(offer2.getCommodity2());
					if(compare == 0) {
						compare = Integer.compare(offer1.getMaxTradeVolume(), offer2.getMaxTradeVolume());
						if(compare == 0) {
							compare = Integer.compare(offer1.getMinReceive(), offer2.getMinReceive());
							return compare;
						} else {
							return compare;
						}
					} else {
						return compare;
					}
				} else {
					return compare;
				}
			}
		};
		
		ArrayList<Offer> orderedOffers = Collections.list(Collections.enumeration(offers));
		Collections.sort(orderedOffers, comparator);
		
		int offerX = this.x + 20;
		int offerY = this.y + 20;
		
		for(int i=0;i<15;i++) {
			Offer offer = orderedOffers.get(i);
			if(offer != null) {
				OfferEntry offerEntry = new OfferEntry(offerX, offerY, 700, 30, offer);
				offerEntry.drawSelf(g);
				
				offerY += 38;
			}
		}
	}
	
}
