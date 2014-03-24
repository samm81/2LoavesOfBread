package core.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import core.MarketSimulation;
import core.commodities.Commodity;

/**
 * The general container for all the games elements.
 * 
 * @author Sam Maynard
 *
 */
@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {
	
	MarketSimulation sim;
	
	LinkedList<Graph> graphs;
	Key key;
	Inventory inventory;
	
	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}
	
	@Override
	void init() {
		graphs = createGraphs(sim.getCommodities(), 200);
		key = new Key(0, 0, this.getWidth(), 40, sim.getCommodities());
		inventory = new Inventory(0, this.getHeight() - 150, this.getWidth(), 150, sim.getCommodities());
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
		int i = 0;
		for(Commodity commodity : commodities) {
			int x = (i % 2) * (this.getWidth() / 2 + 5);
			int y = 45 + (i / 2) * 203;
			graphs.add(new Graph(x, y, graphWidth, graphHeight, commodity));
			i++;
		}
		return graphs;
	}
	
	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);
		
		key.drawSelf(g);
		
		for(Graph graph : graphs) {
			graph.drawSelf(g);
		}
		
		inventory.drawSelf(g);
	}
	
	@Override
	protected void updateVars() {}
	
}
