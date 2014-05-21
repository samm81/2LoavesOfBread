package core.GUI.TickerScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import core.MarketSimulation;
import core.GUI.GraphicalObject;

/**
 * Shows the time remaining
 * @author Sam Maynard
 *
 */
public class Timer extends GraphicalObject {
	
	MarketSimulation sim;
	
	/**
	 * constructor
	 * @param sim the {@link MarketSimulation} that has the timer data
	 */
	public Timer(int x, int y, int width, int height, MarketSimulation sim) {
		super(x, y, width, height);
		this.sim = sim;
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle2D.Float(x, y, width, height);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		g.setFont(new Font("Sans Serif", Font.PLAIN, 30));
		g.setColor(Color.GRAY);
		
		String str = "Time remaining: " + sim.getTimeLeftInSeconds().toString();
		g.drawString(str, x - 30, y + height - 3);
	}
	
}
