package core;

import core.GUI.Ticker;
import core.actors.Actor;
import core.actors.Player;
import core.channels.OfferChannel;
import core.commodities.Commodity;

import java.util.*;
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
	private int playerStartVolumes = 50;
	
	public MarketSimulation(double dt, double offerDT) {
		super(dt);
		this.actors = new HashSet<>();
		this.commodities = Collections.synchronizedList(new LinkedList<Commodity>());
		this.transactions = new LinkedBlockingQueue<>();
		
		//Creates the transaction thread that evaluates offers, every offerDT.
		offerChannel = new OfferChannel(getTransactions(), getActors(), offerDT);
		offerChannel.setDaemon(true);
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
	
	/**
	 * @param actor - Takes in an abstract Actor.
	 */
	public void addActor(Actor actor) {
		assert actor != null : "Null Actor.";
		this.actors.add(actor);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void addCommodity(Commodity commodity) {
		this.commodities.add(commodity);
	}
	
	public void createTickers(int tickerMagnitude) {
		for(Commodity commodity : this.commodities) {
			commodity.createTickersFromCommodities(this.commodities, tickerMagnitude);
		}
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
	
	@Override
	protected void initialize() {
		int[] playerStartingVolumes = new int[this.commodities.size()];
		for(int i=0;i<playerStartingVolumes.length;i++)
			playerStartingVolumes[i] = playerStartVolumes ;
		this.player = new Player(this.commodities, playerStartingVolumes);
		
		actors.add(player);
		
		offerChannel.start();
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
		//Do we want evaluation and update to be sequential or concurrent.
		//Seems smarter to have them operate at same time so actors always have the most up to date info.
		for(Actor actor : this.actors) {
			actor.evaluateMarket();
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
