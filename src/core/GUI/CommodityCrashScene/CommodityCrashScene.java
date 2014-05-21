package core.GUI.CommodityCrashScene;

import java.awt.Color;
import java.awt.Graphics2D;

import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;
import core.commodities.Commodity;

/**
 * {@link Scene} that contains the commodity crash message
 * @author Sam Maynard
 *
 */
public class CommodityCrashScene extends Scene implements Listener {

	Scene underlyingScene;
	
	Commodity commodity;
	
	/**
	 * constructor
	 * @param commodity commodity that has crashed
	 * @param scene scene underlying the message
	 */
	public CommodityCrashScene(int width, int height, Commodity commodity, Scene scene, Listener listener) {
		super(listener);
		this.underlyingScene = scene;
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, Color.WHITE, this);
		graphicalObjects.add(transparencyOverlay);
		
		int w = 800;
		int h = 100;
		int x = width / 2 - w / 2;
		int y = height / 2 - h / 2;
		CommodityCrashPopup commodityCrashPopup = new CommodityCrashPopup(x, y, w, h, commodity);
		graphicalObjects.add(commodityCrashPopup);
	}
	
	public Scene getUnderlyingScene() {
		return this.underlyingScene;
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		underlyingScene.drawSelf(g);
		super.drawSelf(g);
	}

	@Override
	public void hear(String message, Object sender) {
		switch(message){
		case "ClearOverlay":
			listener.hear("TickerScene", this);
			break;
		}
	}
	
}
