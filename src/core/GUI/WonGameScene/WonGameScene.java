package core.GUI.WonGameScene;

import java.awt.Color;

import core.GUI.Listener;
import core.GUI.Scene;
import core.GUI.TransparencyOverlay;

/**
 * {@link Scene} to hold the won game message
 * @author samwm_000
 *
 */
public class WonGameScene extends Scene implements Listener {

	/**
	 * constructor
	 */
	public WonGameScene(int width, int height, Listener listener) {
		super(listener);
		
		TransparencyOverlay transparencyOverlay = new TransparencyOverlay(width, height, Color.WHITE, this);
		graphicalObjects.add(transparencyOverlay);
		
		int w = 800;
		int h = 100;
		int x = width / 2 - w / 2;
		int y = height / 2 - h / 2;
		WonGamePopup wonGamePopup = new WonGamePopup(x, y, w, h);
		graphicalObjects.add(wonGamePopup);
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
