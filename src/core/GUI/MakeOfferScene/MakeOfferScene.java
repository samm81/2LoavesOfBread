package core.GUI.MakeOfferScene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.GUI.TickerScene.GoButton;
import core.GUI.TickerScene.TickerScene;
import core.commodities.Commodity;


public class MakeOfferScene extends Scene implements Listener {

	TickerScene tickerScene;
	
	public MakeOfferScene(int width, int height, List<Commodity> commodities, TickerScene tickerScene) {
		super();
		this.tickerScene = tickerScene;
		
        TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, new Color(1f, 1f, 1f, .6f), this);
		
		int w = 800;
        int h = 100;
        int x = width / 2 - w / 2;
        int y = height / 2 - h / 2;
        MakeOfferPopup makeOfferPopup = new MakeOfferPopup(x, y, w, h, this, commodities);
        GoButton goButton = new GoButton(x + w - 95, y + 25, 75, 50, this);
		
        graphicalObjects.add(transparencyOverlay);
        graphicalObjects.add(makeOfferPopup);
        graphicalObjects.add(goButton);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		tickerScene.drawSelf(g);
		super.drawSelf(g);
	}
	
	@Override
	public void hear(String message) {
		// TODO Auto-generated method stub
		
	}
	
}
