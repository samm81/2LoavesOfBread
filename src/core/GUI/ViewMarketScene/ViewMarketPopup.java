package core.GUI.ViewMarketScene;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import core.Offer;
import core.Transaction;
import core.GUI.GraphicalObject;
import core.GUI.Listener;
import core.actors.Player;
import core.channels.OfferChannel;

public class ViewMarketPopup extends GraphicalObject implements Listener {
	
	OfferChannel offerChannel;
	Player player;
	Listener listener;
	LinkedList<OfferEntry> offerEntries;
	
	Comparator<Offer> noFilterComparator;
	Comparator<Offer> filterComparator;
	
	int offerListingsStart = 0;
	int numOfferListings = 14;
	
	private boolean filtering = true;
	private boolean makingNonReduntant = true;
	
	public ViewMarketPopup(int x, int y, int width, int height, OfferChannel offerChannel, Player player, Listener listener) {
		super(x, y, width, height);
		this.offerChannel = offerChannel;
		this.player = player;
		this.listener = listener;
		
		noFilterComparator = new Comparator<Offer>() {
			
			public int compare(Offer offer1, Offer offer2) {
				int compare = offer1.getCommodity1().compareTo(offer2.getCommodity1());
				if(compare == 0) {
					compare = Integer.compare(offer1.getMaxTradeVolume(), offer2.getMaxTradeVolume());
					if(compare == 0) {
						compare = offer1.getCommodity2().compareTo(offer2.getCommodity2());
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
		
		filterComparator = new Comparator<Offer>() {
			
			public int compare(Offer offer1, Offer offer2) {
				
				if(!(playerCanMakeOffer(offer1) && playerCanMakeOffer(offer2))) {
					if(playerCanMakeOffer(offer1))
						return -1;
					if(playerCanMakeOffer(offer2))
						return 1;
				}
				
				int compare = offer1.getCommodity1().compareTo(offer2.getCommodity1());
				if(compare == 0) {
					compare = Integer.compare(offer1.getMaxTradeVolume(), offer2.getMaxTradeVolume());
					if(compare == 0) {
						compare = offer1.getCommodity2().compareTo(offer2.getCommodity2());
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
	}
	
	public boolean playerCanMakeOffer(Offer offer) {
		Offer playerOffer = new Offer(offer.toTransaction(), player);
		return player.canMakeOffer(playerOffer);
	}
	
	private void scrollUp() {
		if(offerListingsStart > 0) {
			offerListingsStart -= numOfferListings;
		} else {
			listener.hear("Topped", this);
		}
		listener.hear("Unbottomed", this);
	}
	
	private void scrollDown() {
		if(offerListingsStart + numOfferListings <= offerChannel.getOffersMap().size()) {
			offerListingsStart += numOfferListings;
		} else {
			listener.hear("Bottomed", this);
		}
		listener.hear("Untopped", this);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);
		
		Collection<Offer> offers = offerChannel.getOffersMap().values();
		
		if(makingNonReduntant) {
			HashMap<Transaction, Offer> nonRedundantOffers = new HashMap<Transaction, Offer>();
			for(Offer offer : offers) {
				nonRedundantOffers.put(offer.toTransaction(), offer);
			}
			offers = nonRedundantOffers.values();
		}
		
		ArrayList<Offer> orderedOffers = Collections.list(Collections.enumeration(offers));
		for(int i = 0; i < orderedOffers.size(); i++) {
			orderedOffers.set(i, orderedOffers.get(i).reverse());
		}
		
		if(filtering)
			Collections.sort(orderedOffers, filterComparator);
		else
			Collections.sort(orderedOffers, noFilterComparator);
		
		int offerX = this.x + 20;
		int offerY = this.y + 50;
		
		offerEntries = new LinkedList<OfferEntry>();
		for(int i = offerListingsStart; i < offerListingsStart + numOfferListings; i++) {
			Offer offer = null;
			if(i < orderedOffers.size())
				offer = orderedOffers.get(i);
			if(offer != null) {
				boolean acceptable = false;
				Offer playerOffer = new Offer(offer.toTransaction(), player);
				if(player.canMakeOffer(playerOffer))
					acceptable = true;
				
				OfferEntry offerEntry = new OfferEntry(offerX, offerY, 700, 30, acceptable, offer, this);
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
	
	private Offer makePlayerOffer(Offer offer) {
		return new Offer(offer.toTransaction().reverse(), player);
	}
	
	@Override
	public void hear(String message, Object sender) {
		switch(message) {
		case "AcceptOffer":
			Offer offer = ((OfferEntry) sender).getOffer().reverse();
			Offer playerOffer = makePlayerOffer(offer);
			offerChannel.acceptOffers(offer, playerOffer);
			listener.hear("ClearOverlay", this);
			break;
		case "ScrollUp":
			scrollUp();
			break;
		case "ScrollDown":
			scrollDown();
			break;
		case "Filter":
			filtering = !filtering;
			break;
		case "MakeNonRedundant":
			makingNonReduntant = !makingNonReduntant;
		default:
			System.out.println(message);
			break;
		}
	}
	
}
