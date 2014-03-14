package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import core.MarketSimulation;
import core.commodities.Commodity;

@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {
	
	MarketSimulation sim;
	
	LinkedList<Graph> graphs;
	
	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}
	
	@Override
	void init() {
		graphs = createGraphs(sim.getCommodities(), 200);
	}
	
	private LinkedList<Graph> createGraphs(LinkedList<Commodity> commodities, int height) {
		LinkedList<Graph> graphs = new LinkedList<Graph>();
		
		int graphWidth = this.getWidth() / 2 - 5;
		int graphHeight = height;
		for(Commodity commodity : commodities) {
			graphs.add(new Graph(graphWidth, graphHeight, commodity));
		}
		return graphs;
	}
	
	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);
		
		drawKey(0, 0, this.getWidth(), 40, g);
		
		int i = 0;
		for(Graph graph : graphs) {
			int x = (i % 2) * (this.getWidth() / 2 + 5);
			int y = 45 + (i / 2) * 203;
			graph.drawSelf(x, y, g);
			i++;
		}
	}
	
	private void drawKey(int x, int y, int width, int height, Graphics2D g) {
		GUIUtils.drawOutline(x, y, width, height, 10, g);
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		FontMetrics metrics = g.getFontMetrics();
		
		int labelX = x + 10;
		int labelY = y + 15;
		
		g.setColor(Color.BLACK);
		g.drawString("KEY:", labelX, labelY + 11);
		
		labelX += 50;
		
		for(Commodity commodity : sim.getCommodities()) {
			Color color = commodity.getColor();
			g.setColor(color);
			g.fillOval(labelX, labelY, 10, 10);
			String name = commodity.getClass().getSimpleName();
			g.drawString(name, labelX + 15, labelY + 11);
			
			labelX += metrics.stringWidth(name) + 27;
		}
	}
	
	@Override
	protected void updateVars() {}
	
}
