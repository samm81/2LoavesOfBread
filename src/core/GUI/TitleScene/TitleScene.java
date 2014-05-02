package core.GUI.TitleScene;

import core.GUI.Listener;
import core.GUI.Scene;

public class TitleScene extends Scene implements Listener {

	public TitleScene(int width, int height, Listener listener) {
		super(listener);
		
		Title title = new Title(0, 0, width, height, this);
		graphicalObjects.add(title);
	}

	@Override
	public void hear(String message, Object sender) {
		listener.hear("TickerScene", this);
	}
	
}
