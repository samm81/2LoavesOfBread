package core.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

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
	
	protected MarketSimulation sim;
	
	protected LinkedBlockingQueue<GraphicalObject> graphicalObjects;
	
	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}
	
	@Override
	void init() {
		LinkedList<Graph> graphs = createGraphs(sim.getCommodities(), 200);
		Key key = new Key(0, 0, this.getWidth(), 40, this, sim.getCommodities());
		Inventory inventory = new Inventory(0, this.getHeight() - 150, this.getWidth(), 150, this, sim.getCommodities());
		MakeOfferButton makeOfferButton = new MakeOfferButton(this.getWidth() - 250, this.getHeight() - 100, 220, 50, this);
		
		addGraphicalObject(key);
		addGraphicalObject(inventory);
		addGraphicalObject(makeOfferButton);
		for(Graph graph : graphs)
			addGraphicalObject(graph);
	}
	
	/**
	 * Adds a GraphicalObject to the list of GraphicalObjects to
	 * be drawn.
	 * @param graphicalObject GraphicalObject to be added
	 */
	public void addGraphicalObject(GraphicalObject graphicalObject) {
		if(this.graphicalObjects == null)
			this.graphicalObjects = new LinkedBlockingQueue<GraphicalObject>();
		
		this.graphicalObjects.add(graphicalObject);
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
			graphs.add(new Graph(x, y, graphWidth, graphHeight, this, commodity));
			i++;
		}
		return graphs;
	}
	
	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);
		
		for(GraphicalObject graphicalObject : graphicalObjects)
			graphicalObject.drawSelf(g);
		
	}
	
	@Override
	protected void updateVars() {}

	@Override
	protected void processInputs() {
		if(this.mouseClicksWaiting()){
			LinkedList<MouseEvent> clicks = this.flushMouseClickQueue();
			for(MouseEvent click : clicks){
				int x = click.getX();
				int y = click.getY();
				for(GraphicalObject graphicalObject : graphicalObjects){
					if(graphicalObject.pointInBounds(x, y))
						graphicalObject.clicked();
				}
			}
		}
		
		if(this.keyPressesWaiting()){
			LinkedList<KeyEvent> keyPresses = this.flushKeyPressQueue();
			for(KeyEvent keyPress : keyPresses){
				System.out.println(keyPress.getKeyChar() + " pressed");
			}
		}
	}

	public void message(String message) {
		switch(message){
		case "MakeOfferOverlay":
			Color color = new Color(1f, 1f, 1f, .5f);
			TransparencyOverlay overlay = new TransparencyOverlay(this, color);
			addGraphicalObject(overlay);
			break;
		}
	}
	
}
