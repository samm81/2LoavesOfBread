package core.GUI.ViewMarketScene;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import core.Offer;
import core.GUI.GraphicalObject;
import core.GUI.Listener;
import core.actors.Player;
import core.channels.OfferChannel;

public class ViewMarketPopup extends GraphicalObject implements Listener {
	
	OfferChannel offerChannel;
	Player player;
	Listener listener;
	LinkedList<OfferEntry> offerEntries;
	
	public ViewMarketPopup(int x, int y, int width, int height, OfferChannel offerChannel, Player player, Listener listener) {
		super(x, y, width, height);
		this.offerChannel = offerChannel;
		this.player = player;
		this.listener = listener;
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
		
		offerEntries = new LinkedList<OfferEntry>();
		for(int i = 0; i < 15; i++) {
			Offer offer = orderedOffers.get(i);
			if(offer != null) {
				OfferEntry offerEntry = new OfferEntry(offerX, offerY, 700, 30, offer, this);
				offerEntry.drawSelf(g);
				
				offerEntries.add(offerEntry);
				
				offerY += 38;
			}
		}
	}
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		for(OfferEntry offerEntry : offerEntries)
			if(offerEntry.pointInBounds(click.getX(), click.getY()))
				offerEntry.clicked(click);
	}

	@Override
	public void hear(String message, Object sender) {
		switch(message){
		case "AcceptOffer":
			OfferEntry entry = (OfferEntry) sender;
			Offer offer = entry.getOffer();
			Offer playerOffer = new Offer(offer.getCommodity2(), offer.getCommodity1(), offer.getMinReceive(), offer.getMaxTradeVolume(), player);
			offerChannel.acceptOffers(offer, playerOffer);
			listener.hear("ClearOverlay", this);
			break;
		}
	}
	
}
