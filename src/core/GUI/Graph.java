package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.commodities.Commodity;
import core.commodities.Ticker;


public class Graph {

	private int width;
	private int height;
	private Commodity commodity;
	
	public Graph(int width, int height, Commodity commodity) {
		this.width = width;
		this.height = height;
		this.commodity = commodity;
	}

	public void drawSelf(int x, int y, Graphics2D g) {
		GUIUtils.drawOutline(x, y, width, height, 50, g);

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
		
		g.setColor(new Color(.3f, .3f, .3f));
		double dy = tickerHeight / 7d;
		for(int i = 0; i <= 7; i++) {
			int x1 = tickerx;
			int y1 = tickery + (int) (i * dy);
			int x2 = tickerx + tickerWidth;
			int y2 = y1;
			g.drawLine(x1, y1, x2, y2);
		}
		
		int i = 0;
		for(Ticker ticker : commodity.getTickerCollection()) {
			ticker.drawSelf(tickerx, tickery, tickerWidth, tickerHeight, g);
			ticker.drawLabel(tickerx + i * 25, tickery - 1, tickerHeight, 7, g);
			i++;
		}
	}
	
}
