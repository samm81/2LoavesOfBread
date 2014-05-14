package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import core.Offer;
import core.GUI.GraphicalObject;
import core.GUI.Listener;
import core.commodities.Commodity;

public class OfferEntry extends GraphicalObject implements Listener {
	
	private Offer offer;
	private Listener listener;
	private AcceptOfferButton acceptOfferButton;
	
	public OfferEntry(int x, int y, int width, int height, boolean acceptable, Offer offer, Listener listener) {
		super(x, y, width, height);
		this.offer = offer;
		this.listener = listener;
		
		this.acceptOfferButton = new AcceptOfferButton(x + 600, y + 3, 75, height - 6, Color.GREEN, this);
		acceptOfferButton.setClickable(acceptable);
	}
	
	public Offer getOffer() {
		return this.offer;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 10, 10);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(new Color(1f, .8f, .8f), 1, g);
		
		int offerX = this.x + 20;
		int offerY = this.y + 25;
		
		int volume1 = offer.getMaxTradeVolume();
		int volume2 = offer.getMinReceive();
		Commodity commodity1 = offer.getCommodity1();
		Commodity commodity2 = offer.getCommodity2();
		
		drawString("for", offerX, offerY, g);
		offerX += 50;
		drawNum(volume1, offerX, offerY, g);
		offerX += 50;
		drawCommodity(commodity1, offerX, offerY, g);
		offerX += 170;
		drawString(" get ", offerX, offerY, g);
		offerX += 70;
		drawNum(volume2, offerX, offerY, g);
		offerX += 50;
		drawCommodity(commodity2, offerX, offerY, g);
		
		acceptOfferButton.drawSelf(g);
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
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		if(acceptOfferButton.pointInBounds(click.getX(), click.getY()))
			acceptOfferButton.clicked(click);
	}
	
	@Override
	public void hear(String message, Object sender) {
		listener.hear(message, this);
	}
	
}
