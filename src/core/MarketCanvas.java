package core;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
			int x = 15 + ((this.getWidth() / 2 - 15) * (i % 2));
			int y = 60 + 205 * (i / 2);
			int width = this.getWidth() / 2 - 20;
			int height = 200;
			drawGraph(x, y, width, height, commodity, g);
			i++;
		}
		
		drawKey(15, 10, this.getWidth() - 35, 40, g);

	}

	private void drawKey(int x, int y, int width, int height, Graphics2D g) {
		drawOutline(x, y, width, height, 10, g);
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		FontMetrics metrics = g.getFontMetrics();

		int labelX = x + 10;
		int labelY = y + 15;
		
		g.setColor(Color.BLACK);
		g.drawString("KEY:", labelX, labelY + 11);
		
		labelX += 50;
		
		for(Commodity commodity : sim.getCommodities()){
			Color color = commodity.getColor();
			g.setColor(color);
			g.fillOval(labelX, labelY, 10, 10);
			String name = commodity.getClass().getSimpleName();
			g.drawString(name, labelX + 15, labelY + 11);
			
			labelX += metrics.stringWidth(name) + 27;
		}
	}


	private void drawGraph(int x, int y, int width, int height, Commodity commodity, Graphics2D g) {
		drawOutline(x, y, width, height, 50, g);
		
		int titlex = x + 30;
		int titley = y + 30;
		String name = commodity.getClass().getSimpleName();
		g.setFont(new Font("Sans Serif", Font.BOLD, 22));
		g.setColor(Color.BLACK);
		g.drawString(name, titlex, titley);

		int tickerx = x + 30;
		int tickery = y + 50;
		int tickerWidth = width - 65;
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

	private void drawOutline(int x, int y, int width, int height, int rounding, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, rounding, rounding);
		int offset = 3;
		g.setColor(Color.WHITE);
		// g.fillRoundRect(x + offset, y + offset, width - offset, height - offset, 50, 50); // cool effect
		g.fillRoundRect(x + offset, y + offset, width - offset * 2, height - offset * 2, rounding, rounding);
	}

	@Override
	protected void updateVars() {
		
	}

}
