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
public class MarketSimulation extends Simulation {
	
	protected Player player;
	protected HashSet<Actor> actors;
	protected List<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	protected OfferChannel offerChannel;
	
	protected Long time;
	protected Long last;
	
	public MarketSimulation(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions, Player player, HashSet<Actor> actors, OfferChannel offerChannel, double dt) {
		super(dt);
		this.actors = actors;
		this.commodities = commodities;
		this.transactions = transactions;
		this.player = player;
		this.offerChannel = offerChannel;
		time = (long) (60 * 3 * 1000);
	}
	
	public LinkedBlockingQueue<Transaction> getTransactions() {
		return this.transactions;
	}
	
	public HashSet<Actor> getActors() {
		return this.actors;
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
	
	public Long getTime() {
		return this.time;
	}
	
	public Integer getTimeInSeconds() {
		return (int) (this.time / 1000);
	}
	
	@Override
	protected void initialize() {
		last = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			tick();
			try {
				Thread.yield();
			} finally {
				try {
					Thread.sleep((long) (this.dt * 1000));
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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
		
		long now = System.currentTimeMillis();
		time -= now - last;
		last = now;
		
		//Do we want evaluation and update to be sequential or concurrent.
		//Seems smarter to have them operate at same time so actors always have the most up to date info.
		for(Actor actor : this.actors) {
			actor.evaluateMarket(offerChannel);
		}
		
		// updates the tickers with the most recent ratio
		for(Commodity commodity : this.commodities) { // go through all the commodities
			HashMap<String, Ticker> tickers = commodity.getTickers(); // get all the tickers for that commodity
			for(Entry<String, Ticker> entry : tickers.entrySet()) { // find the most recent transaction value for each ticker commodity, and update the ticker
				Ticker ticker = entry.getValue();
				String tickerName = entry.getKey();
				double dataPoint = commodity.getMostRecentRatios().get(tickerName);
				try {
					ticker.addDataPoint(dataPoint);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
