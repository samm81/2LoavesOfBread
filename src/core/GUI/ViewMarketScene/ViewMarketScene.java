package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.GUI.TickerScene.TickerScene;
import core.channels.OfferChannel;


public class ViewMarketScene extends Scene implements Listener {
	
	TickerScene tickerScene;
	
	public ViewMarketScene(int width, int height, OfferChannel offerChannel, TickerScene tickerScene) {
		super();
		this.tickerScene = tickerScene;
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, new Color(1f, 1f, 1f, .6f), this);
		
		int w = 800;
        int h = 600;
        int x = width / 2 - w / 2;
        int y = height / 2 - h / 2;
        ViewMarketPopup viewMarketPopup = new ViewMarketPopup(x, y, w, h, offerChannel);
        
        graphicalObjects.add(transparencyOverlay);
        graphicalObjects.add(viewMarketPopup);
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
