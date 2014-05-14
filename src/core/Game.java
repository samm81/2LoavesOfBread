package core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import core.GUI.MarketCanvas;
import core.actors.Actor;
import core.actors.Baker;
import core.actors.Farmer;
import core.actors.Fisherman;
import core.actors.Merchant;
import core.actors.Player;
import core.channels.OfferChannel;
import core.commodities.Commodity;

public class Game extends TickableThread {

	final double dt = 2d;
	final double offerDT = dt;
	final long timeLimit = 60 * 8 * 1000;
	final int numActors = 400;
	int tickerMagnitude = 30;
	
	private MarketSimulation sim;
	private LinkedList<Commodity> commodities;
	private LinkedBlockingQueue<Transaction> transactions;
	private Player player;
	private HashSet<Actor> actors;
	private OfferChannel offerChannel;
	private MarketCanvas marketCanvas;

	public Game() {
		super(1);
		initializeObjects();
		sim = new MarketSimulation(commodities, transactions, player, actors, offerChannel, timeLimit, dt);
		marketCanvas = new MarketCanvas(60, sim, this);
	}
	
	private void initializeObjects() {
		commodities = new LinkedList<Commodity>();
		for(Commodity item : Commodity.values())
			commodities.add(item);
		
		for(Commodity commodity : commodities)
			commodity.createTickersFromCommodities(commodities, tickerMagnitude);
		
		transactions = new LinkedBlockingQueue<Transaction>();
		
		player = new Player(commodities, new int[] { 2, 0, 0, 0 }, new int[] { 0, 0, 0, 40 });
		
		actors = new HashSet<Actor>();
		for(int i = 0; i < numActors; i++) {
			Random r = new Random();
			switch(r.nextInt(4)) {
			case 0:
				actors.add(new Baker(commodities, transactions));
				break;
			case 1:
				actors.add(new Farmer(commodities, transactions));
				break;
			case 2:
				actors.add(new Fisherman(commodities, transactions));
				break;
			case 3:
				actors.add(new Merchant(commodities, transactions));
				break;
			}
		}
		actors.add(player);
		
		offerChannel = new OfferChannel(transactions, actors, offerDT, actors.size());
		offerChannel.setDaemon(true);
	}
	
	public MarketCanvas getMarketCanvas() {
		return this.marketCanvas;
	}
	
	public void play() {
		this.start();
	}

	@Override
	protected void initialize() {
		sim.start();
		offerChannel.start();
	}

	@Override
	protected void tick() {
		long timeLeft = sim.getTimeLeft();
		if(timeLeft < 0){
			marketCanvas.hear("GameLost", sim);
		}
	}


}
