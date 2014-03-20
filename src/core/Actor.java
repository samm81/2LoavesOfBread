package core;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import core.commodities.Commodity;

/**
 * Abstract class the represents every player.
 * 
 * @author Sam "Fabulous Hands" Maynard
 * 
 */
abstract class Actor {
	
	protected LinkedList<Commodity> commodities; // list of global commodities
	protected LinkedBlockingQueue<Transaction> transactions; // list of global transactions
	
	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
	}
	
	// patrick:
	// actor figures out what they want the most right now, and places an open offer
	// for how much they are willing to trade for it
	public abstract Transaction getBestOffer();
	
	// patrick:
	// actor should look at their goods, their wants, and the market
	// then reevaluate how much they are willing to trade for each object
	public abstract void evaluateMarket();
	
}