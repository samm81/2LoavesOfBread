package core;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import core.actors.Actor;
import core.commodities.Commodity;
import core.commodities.Ticker;

/**
 * The main simulation running the game. Contains all the data objects, and
 * is given to the canvas in order to draw the graphs.
 * 
 * @author Sam "Fabulous Hands" Maynard
 * 
 */
public class MarketSimulation extends Simulation {
	
	protected HashSet<Actor> actors;
	protected LinkedList<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	
	public MarketSimulation(double dt) {
		super(dt);
		this.actors = new HashSet<Actor>();
		this.commodities = new LinkedList<Commodity>();
		this.transactions = new LinkedBlockingQueue<Transaction>();
	}
	
	public LinkedBlockingQueue<Transaction> getTransactions() {
		return transactions;
	}
	
	public HashSet<Actor> getActors() {
		return this.actors;
	}
	
	public LinkedList<Commodity> getCommodities() {
		return this.commodities;
	}
	
	public void addActor(Actor actor) {
		this.actors.add(actor);
	}
	
	public void addCommodity(Commodity commodity) {
		this.commodities.add(commodity);
	}
	
	public void createTickers(int tickerMagnitude) {
		for(Commodity commodity : commodities) {
			commodity.createTickersFromCommodities(commodities, tickerMagnitude);
		}
	}
	
	@Override
	protected void initialize() {}
	
	@Override
	protected void tick() {
		//Do we want evaluation and update to be sequential or concurrent.
		//Seems smarter to have them operate at same time so actors always have the most up to date info.
		for(Actor actor : this.actors) {
			actor.evaluateMarket();
		}
		
		// START TEMP CODE
		// will be actors making their offers to the market
		// and then the market matching the offers
		Random r = new Random();
		int commodity1Index = r.nextInt(this.commodities.size());
		int commodity2Index = 0;
		do {
			commodity2Index = r.nextInt(this.commodities.size());
		} while(commodity2Index == commodity1Index);
		Commodity commodity1 = this.commodities.get(commodity1Index);
		Commodity commodity2 = this.commodities.get(commodity2Index);
		
		double ratio = commodity1.getMostRecentRatios().get(commodity2.getClass());
		double trade = 0;
		if(ratio == 0) {
			trade = r.nextInt(9);
		} else {
			do {
				trade = ratio + r.nextDouble() / 2 - .25;
			} while(!(trade > 0));
		}
		//Line below creates problems, as there is no Actor to reference who made the trade
		//commodity1.addTransaction(new Transaction(1, commodity1, trade, commodity2));
		// END TEMP CODE
		
		// updates the tickers with the most recent ratio
		for(Commodity commodity : commodities) { // go through all the commodities
			Hashtable<Class<? extends Commodity>, Ticker> tickers = commodity.getTickers(); // get all the tickers for that commodity
			for(Entry<Class<? extends Commodity>, Ticker> entry : tickers.entrySet()) { // find the most recent transaction value for each ticker commodity, and update the ticker
				Ticker ticker = entry.getValue();
				Class<? extends Commodity> tickerClass = entry.getKey();
				double dataPoint = commodity.getMostRecentRatios().get(tickerClass);
				try {
					ticker.addDataPoint(dataPoint);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
