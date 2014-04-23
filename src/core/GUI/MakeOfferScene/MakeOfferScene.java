package core.GUI.MakeOfferScene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import core.Transaction;
import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.GUI.TickerScene.TickerScene;
import core.commodities.Commodity;

public class MakeOfferScene extends Scene implements Listener {
	
	TickerScene tickerScene;
	MakeOfferDialog makeOfferPopup;
	GoButton goButton;
	
	public MakeOfferScene(int width, int height, List<Commodity> commodities, TickerScene tickerScene, Listener listener) {
		super(listener);
		this.tickerScene = tickerScene;
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, new Color(1f, 1f, 1f, .6f), this);
		
		int w = 800;
		int h = 100;
		int x = width / 2 - w / 2;
		int y = height / 2 - h / 2;
		MakeOfferDialog makeOfferPopup = new MakeOfferDialog(x, y, w, h, this, commodities);
		GoButton goButton = new GoButton(x + w - 95, y + 25, 75, 50, this);
		
		graphicalObjects.add(transparencyOverlay);
		graphicalObjects.add(makeOfferPopup);
		graphicalObjects.add(goButton);
		
		this.makeOfferPopup = makeOfferPopup;
		this.goButton = goButton;
	}
	
	public Transaction getSubmittedTransaction() {
		Commodity commodity1 = makeOfferPopup.getCommodity1();
		Commodity commodity2 = makeOfferPopup.getCommodity2();
		Integer volume1 = makeOfferPopup.getVolume1();
		Integer volume2 = makeOfferPopup.getVolume2();
		if(volume1 == null || volume2 == null)
			return null;
		
		return new Transaction(volume1, commodity1, volume2, commodity2);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		tickerScene.drawSelf(g);
		if(getSubmittedTransaction() != null)
			goButton.setClickable(true);
		else
			goButton.setClickable(false);
		super.drawSelf(g);
	}
	
	@Override
	public void hear(String message) {
		switch(message) {
		case "ClearOverlay":
			listener.hear("TickerScene");
			break;
		default:
			listener.hear(message);
		}
	}
	
}
