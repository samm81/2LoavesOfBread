package core.GUI.EndGameScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import core.GUI.Button;
import core.GUI.Listener;

/**
 * Button that allows for a restart of game
 * @author Sam Maynard
 *
 */
public class RestartButton extends Button {
	
	/**
	 * constructor
	 */
	public RestartButton(int x, int y, int width, int height, Listener listener) {
		super(x, y, width, height, Color.WHITE, "Restart?", "Restart", listener);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 20, 20);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(g);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Sans Serif", Font.PLAIN, 26));
		g.drawString(text, x + 17, y + height - 16);
	}
	
}
