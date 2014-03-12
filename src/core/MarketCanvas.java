package core;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import core.commodities.Commodity;
import core.commodities.Ticker;

@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {

	MarketSimulation sim;

	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}

	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);

		int i = 0;
		for(Commodity commodity : sim.getCommodities()) {
			int x = 10 + (this.getWidth() / 2 * (i % 2));
			int y = 60 + 210 * (i / 2);
			int width = this.getWidth() / 2 - 20;
			int height = 200;
			drawGraph(x, y, width, height, commodity, g);
			i++;
		}

	}

	private void drawGraph(int x, int y, int width, int height, Commodity commodity, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, 50, 50);
		int offset = 3;
		g.setColor(Color.WHITE);
		// g.fillRoundRect(x + offset, y + offset, width - offset, height - offset, 50, 50); // cool effect
		g.fillRoundRect(x + offset, y + offset, width - offset * 2, height - offset * 2, 50, 50);

		int titlex = x + 30;
		int titley = y + 30;
		String name = commodity.getClass().getSimpleName();
		g.setFont(new Font("Sans Serif", Font.BOLD, 22));
		g.setColor(Color.BLACK);
		g.drawString(name, titlex, titley);

		int tickerx = x + 30;
		int tickery = y + 50;
		int tickerWidth = width - 50;
		int tickerHeight = height - 70;

		g.setColor(new Color(.1f, .1f, .1f));
		double dy = tickerHeight / 7d;
		for(int i = 0; i <= 7; i++) {
			int x1 = tickerx;
			int y1 = tickery + (int) (i * dy);
			int x2 = tickerx + tickerWidth;
			int y2 = y1;
			g.drawLine(x1, y1, x2, y2);
		}

		for(Ticker ticker : commodity.getTickerCollection()) {
			ticker.drawSelf(tickerx, tickery, tickerWidth, tickerHeight, g);
		}
	}

	@Override
	protected void updateVars() {
		
	}

}
