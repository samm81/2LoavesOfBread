package core;

import core.GUI.Ticker;
import core.actors.Actor;
import core.actors.Player;
import core.channels.OfferChannel;
import core.commodities.Commodity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The main simulation running the game. Contains all the data objects, and
 * is given to the canvas in order to draw the graphs.
 * 
 * @author Sam "Fabulous Hands" Maynard
 */
public class MarketSimulation extends TickableThread {
	
	protected Player player;
	protected HashSet<Actor> actors;
	protected List<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	protected OfferChannel offerChannel;
	
	private long timeLimit;
	private long startTime;
	
	public MarketSimulation(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions, Player player, HashSet<Actor> actors, OfferChannel offerChannel, long timeLimit, double dt) {
		super(dt);
		this.actors = actors;
		this.commodities = commodities;
		this.transactions = transactions;
		this.player = player;
		this.offerChannel = offerChannel;
		this.timeLimit = timeLimit;
	}
	
	public List<Commodity> getCommodities() {
		return this.commodities;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public OfferChannel getOfferChannel() {
		return this.offerChannel;
	}
	
	public Long getTimeLeft() {
		return timeLimit - (time - startTime);
	}
	
	public Integer getTimeLeftInSeconds() {
		return (int) (getTimeLeft() / 1000);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * The main engine behind the game
	 * tick goes through every actor, gets their best offer
	 * compares all offers to each other, finds offers
	 * that match, carry out the offers, inform the actors,
	 * and inform the commodities.
	 */
	@Override
	protected void tick() {
		for(Actor actor : this.actors) {
			actor.evaluateMarket(offerChannel);
		}
		
		// updates the tickers with the most recent ratio
		for(Commodity commodity : this.commodities) { // go through all the commodities
			HashMap<Commodity, Ticker> tickers = commodity.getTickers(); // get all the tickers for that commodity
			for(Entry<Commodity, Ticker> entry : tickers.entrySet()) { // find the most recent transaction value for each ticker commodity, and update the ticker
				Ticker ticker = entry.getValue();
				Commodity tickerCommodity = entry.getKey();
				double dataPoint = commodity.getAverageRatio(tickerCommodity);
				if(dataPoint < 0)
					dataPoint = commodity.getLastAverage(tickerCommodity);
				
				try {
					ticker.addDataPoint(dataPoint);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
