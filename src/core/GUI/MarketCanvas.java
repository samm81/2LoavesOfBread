package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import core.MarketSimulation;
import core.commodities.Commodity;

/**
 * The general container for all the games elements.
 * 
 * @author Sam "Fabulous Hands" Maynard
 *
 */
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
	
	/**
	 * generates the graphs from a list of commodities
	 * @param commodities the commodities to generate graphs from
	 * @param height the height of the graphs
	 * @return LinkedList<Graph> of the graphs created
	 */
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
		
		drawInventory(0, this.getHeight() - 150, this.getWidth(), 150, g);
	}

	/**
	 * Draws the key at the top of the screen
	 * 
	 * @param x x coord to draw the key at
	 * @param y y coord to draw the key at
	 * @param width width of the key
	 * @param height height of the key
	 * @param g Graphics2D object to draw the key with
	 */
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

		/**
		 * draws the inventory: player's amount of stuff, the examine trades button
		 * and the make trade button
		 * 
		 * @param x x coord to draw the inventory at
		 * @param y y coord to draw the inventory at
		 * @param width width of the inventory
		 * @param height height of the inventory
		 * @param g Graphics2D object to draw the inventory with
		 */
	private void drawInventory(int x, int y, int width, int height, Graphics2D g) {
		GUIUtils.drawOutline(x, y, width, height, 15, g);
		//TODO
	}
	
	@Override
	protected void updateVars() {}
	
}
