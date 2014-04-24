package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.GUI.TickerScene.TickerScene;
import core.actors.Player;
import core.channels.OfferChannel;


public class ViewMarketScene extends Scene implements Listener {
	
	TickerScene tickerScene;
	
	public ViewMarketScene(int width, int height, OfferChannel offerChannel, Player player, TickerScene tickerScene, Listener listener) {
		super(listener);
		this.tickerScene = tickerScene;
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, new Color(1f, 1f, 1f, .6f), this);
		
		int w = 800;
        int h = 600;
        int x = width / 2 - w / 2;
        int y = height / 2 - h / 2;
        ViewMarketPopup viewMarketPopup = new ViewMarketPopup(x, y, w, h, offerChannel, player, this);
        
        graphicalObjects.add(transparencyOverlay);
        graphicalObjects.add(viewMarketPopup);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		tickerScene.drawSelf(g);
		super.drawSelf(g);
	}
	
	@Override
	public void hear(String message, Object sender) {
		switch(message) {
		case "ClearOverlay":
			listener.hear("TickerScene", this);
			break;
		default:
			listener.hear(message, this);
		}
	}
	
}
