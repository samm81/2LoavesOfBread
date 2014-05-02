package core.GUI.TitleScene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import core.GUI.Button;
import core.GUI.Listener;

public class PlayButton extends Button {
	
	public PlayButton(int x, int y, int width, int height, Listener listener) {
		super(x, y, width, height, Color.WHITE, "", "Play", listener);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle2D.Float(x, y, width, height);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {}
	
}
