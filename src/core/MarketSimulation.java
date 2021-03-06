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
 * @author Sam Maynard
 */
public class MarketSimulation extends TickableThread {
	
	protected Player player; // player that is trading
	protected HashSet<Actor> actors; // list of all actors (including player)
	protected List<Commodity> commodities; // commodities of the simulation
	protected LinkedBlockingQueue<Transaction> transactions; // list of global transactions
	protected OfferChannel offerChannel; // the offer channel where offers are submitted
	
	protected double[][] totalexchange;
	protected double[] totalmarketshare;
	
	private long timeLimit; // the time limit the player has
	private long startTime; // the time the sim started
	
	/**
	 * constructor
	 * @param commodities commodities of the simulation
	 * @param transactions list of global transactions
	 * @param player player that is trading
	 * @param actors list of all actors (including player)
	 * @param offerChannel the offer channel where offers are submitted
	 * @param timeLimit the time limit the player has
	 * @param dt how often to tick
	 */
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
		this.totalexchange = new double[commodities.size()][commodities.size()];
		this.totalmarketshare = new double[commodities.size()];
		for(int i = 0; i < totalmarketshare.length; i++) {
			totalmarketshare[i] = 0;
			for(int j = 0; j < totalexchange[i].length; j++) {
				totalexchange[i][j] = 0;
			}
		}
		
		for(Actor actor : this.actors) {
			actor.evaluateMarket(offerChannel);
			actor.addValues(totalexchange, totalmarketshare);
		}
		/*
		 * for(int i = 0; i < totalexchange.length; i++)
		 * {
		 * totalmarketshare[i]/=this.actors.size();
		 * }
		 */

		/*
		System.out.println("World total market share for each item: ");
		for(int i = 0; i < totalexchange.length; i++) {
			System.out.print(commodities.get(i).name().charAt(0) + ": " + totalmarketshare[i] + " ");
			for(int j = 0; j < totalexchange[i].length; j++) {
				totalexchange[i][j] /= this.actors.size();
			}
		}

		System.out.println("\nExchange rate average");
		for(int i = 0; i < commodities.size(); i++) {
			System.out.print("\t" + commodities.get(i));
		}
		System.out.print("\n");
		for(int i = 0; i < totalexchange.length; i++) {
			System.out.print(commodities.get(i).name().charAt(0));
			for(int j = 0; j < totalexchange[i].length; j++) {
				
				System.out.printf("\t %.2f", totalexchange[i][j]);
			}
			System.out.print("\n");
		}
		*/
		
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
