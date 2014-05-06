package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GUI.DownScrollButton;
import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.GUI.UpScrollButton;
import core.GUI.TickerScene.TickerScene;
import core.actors.Player;
import core.channels.OfferChannel;


public class ViewMarketScene extends Scene implements Listener {
	
	TickerScene tickerScene;
	
	UpScrollButton upScroll;
	DownScrollButton downScroll;
	
	public ViewMarketScene(int width, int height, OfferChannel offerChannel, Player player, TickerScene tickerScene, Listener listener) {
		super(listener);
		this.tickerScene = tickerScene;
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, new Color(1f, 1f, 1f, .6f), this);
		graphicalObjects.add(transparencyOverlay);
		
		int w = 800;
        int h = 600;
        int x = width / 2 - w / 2;
        int y = height / 2 - h / 2;
        ViewMarketPopup viewMarketPopup = new ViewMarketPopup(x, y, w, h, offerChannel, player, this);
        graphicalObjects.add(viewMarketPopup);

        int scrollButtonX = x + w - 60;
        int scrollButtonY = y + 60;
        int scrollButtonHeight = h/2 - 50;
        int scrollButtonWidth = 40;
        upScroll = new UpScrollButton(scrollButtonX, scrollButtonY, scrollButtonWidth, scrollButtonHeight, Color.WHITE, viewMarketPopup);
        scrollButtonY = y + h/2 + 12;
        downScroll = new DownScrollButton(scrollButtonX, scrollButtonY, scrollButtonWidth, scrollButtonHeight, Color.WHITE, viewMarketPopup);
        upScroll.setClickable(false);
        graphicalObjects.add(upScroll);
        graphicalObjects.add(downScroll);
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
		case "Topped":
			upScroll.setClickable(false);
			break;
		case "Untopped":
			upScroll.setClickable(true);
			break;
		default:
			listener.hear(message, this);
			break;
		}
	}
	
}
