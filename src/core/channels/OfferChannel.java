package core.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import core.Offer;
import core.Transaction;
import core.actors.Actor;
import core.commodities.Commodity;

/**
 * 
 * {@link #OfferChannel(java.util.concurrent.LinkedBlockingQueue, java.util.HashSet, double)} - Basic Constructor for the thread.
 * {@link #run()} - Calls tick, then yields to main thread
 * {@link #tick()} - Every dt it gets all the new offers from the actors, and then tries to match them.
 * {@link #isViable(core.Offer, core.Offer)} - Method That ensures that two offers are viable with one another.
 * 
 * @author Brian Oluwo
 */
public class OfferChannel extends Thread {
	
	public Thread thread;
	protected HashSet<Actor> actors;
	protected LinkedBlockingQueue<Transaction> globalTransactions;
	protected HashMap<Actor, Offer> offersMap;
	private double dt;
	
	public OfferChannel(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, double dt) {
		this.globalTransactions = globalTransactions;
		this.actors = actors;
		this.dt = dt;
		this.thread = new Thread(this);
		this.offersMap = new HashMap<Actor, Offer>();
	}
	
	/**
	 *
	 */
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
	 * Run every dt to get the actor's best offers and then process them
	 */
	private void tick() {
		for(Actor actor : this.actors) {
			Offer offer = actor.getBestOffer();
			offersMap.put(actor, offer);
		}
		processOffers();
	}
	
	/**
	 * Processes the offers in the queue
	 */
	private void processOffers() {
		ArrayList<Offer> offers = Collections.list(Collections.enumeration(offersMap.values()));
		Collections.shuffle(offers);
		
		for(int i = 0; i < offers.size(); i++) {
			Offer first = offers.get(i);
			for(int j = i + 1; j < offers.size(); j++) {
				Offer second = offers.get(j);
				if(isViable(first, second)) {
					Transaction t = new Transaction(first.getMinReceive(), first.getCommodity2(), second.getMinReceive(), first.getCommodity1(), first.getSender());
					Transaction q = new Transaction(second.getMinReceive(), second.getCommodity2(), first.getMinReceive(), second.getCommodity1(), second.getSender());
					
					t.getSender().acceptTransaction(t);
					q.getSender().acceptTransaction(q);
					
					t.getCommodity1().addTransaction(t);
					t.getCommodity2().addTransaction(q);
					try {
						this.globalTransactions.put(t);
						this.globalTransactions.put(q);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					
					offers.remove(j);
					
					//System.out.println("Matched offer " + first + " with " + second);
					break;
				}
			}
		}
	}
	
	/**
	 * Conditions to be checked:
	 * 1. They get at least minReceive
	 * 2. They give away no more than maxOffer
	 * 
	 * @param first - The original offer which is checking for viability against offer 2.
	 * @param second - The offer to be compared against
	 * @return Whether or not this offer should be processed
	 */
	public boolean isViable(Offer first, Offer second) {
		// No longer is there a reverse transaction so one needs to check reverses.
		if(first == null || second == null)
			return false;
		if(!first.getCommodity1().name().equals(second.getCommodity2().name()) || !first.getCommodity2().name().equals(second.getCommodity1().name()))
			return false;
		if(first.getMinReceive() > second.getMaxTradeVolume() || second.getMinReceive() > first.getMaxTradeVolume())
			return false;
		
		return true;
	}
	
	/**
	 * @param commodity - The commodity to be inquired about.
	 * @return - # of Offers that offer commodity as their trading away commodity
	 */
	public int getNumberOfOffers(Commodity commodity) {
		Collection<Offer> offers = offersMap.values();
		int count = 0;
		for(Offer offer : offers) {
			if(offer.getCommodity1().equals(commodity))
				count++;
		}
		return count;
	}
	
	/**
	 * Specific for between two commodities. Takes longer.
	 * 
	 * @param tradeAway - Trading Away Commodity
	 * @param tradeFor - Trading For Commodity
	 * @return - Number of Trades trading away tradeAway, for tradeFor
	 */
	public int getNumberOfOffers(Commodity tradeAway, Commodity tradeFor) {
		Collection<Offer> offers = offersMap.values();
		int count = 0;
		for(Offer offer : offers) {
			if(offer.getCommodity1().equals(tradeAway) && offer.getCommodity2().equals(tradeFor))
				count++;
		}
		return count;
	}
}
