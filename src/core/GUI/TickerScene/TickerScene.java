package core.GUI.TickerScene;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import core.MarketSimulation;
import core.GUI.Graph;
import core.GUI.Listener;
import core.GUI.Scene;
import core.actors.Player;
import core.commodities.Commodity;

public class TickerScene extends Scene implements Listener {
	
	public TickerScene(int width, int height, List<Commodity> list, Player player, MarketSimulation sim, Listener listener) {
		super(listener);
		
		Key key = new Key(0, 0, width, 40, list);
		graphicalObjects.add(key);
		
		Inventory inventory = new Inventory(0, height - 150, width, 150, list, player);
		graphicalObjects.add(inventory);
		
		GenericButton makeOfferButton = new GenericButton(width - 250, height - 125, 220, 50, new Color(.31f, .84f, .92f), "MAKE OFFER", "MakeOffer", this);
		GenericButton viewMarketButton = new GenericButton(width - 250, height - 70, 220, 50, new Color(.31f, .84f, .92f), "VIEW MARKET", "ViewMarket", this);
		graphicalObjects.add(makeOfferButton);
		graphicalObjects.add(viewMarketButton);
		
		LinkedList<Graph> graphs = createGraphs(width, list, 200);
		for(Graph graph : graphs)
			graphicalObjects.add(graph);
		
		Timer timer = new Timer(width - 290, height - 205, 100, 50, sim);
		graphicalObjects.add(timer);
	}
	
	/**
	* generates the graphs from a list of commodities
	*
	* @param width the width of the scene
	* @param list the commodities to generate graphs from
	* @param graphHeight the height of the graphs
	* @return LinkedList<Graph> of the graphs created
	*/
	private LinkedList<Graph> createGraphs(int width, List<Commodity> list, int graphHeight) {
		LinkedList<Graph> graphs = new LinkedList<>();
		
		int graphWidth = width / 2 - 5;
		int i = 0;
		for(Commodity commodity : list) {
			int x = (i % 2) * (width / 2 + 5);
			int y = 45 + (i / 2) * 203;
			graphs.add(new Graph(x, y, graphWidth, graphHeight, commodity));
			i++;
		}
		return graphs;
	}
	
	@Override
	public void hear(String message, Object sender) {
		listener.hear(message, this);
	}
	
}
