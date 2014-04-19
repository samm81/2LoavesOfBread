package core.channels;

import core.Offer;
import core.Transaction;
import core.actors.Actor;
import core.commodities.Commodity;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import static core.commodities.Commodity.values;

/**
 * 
 * {@link #OfferChannel(java.util.concurrent.LinkedBlockingQueue, java.util.HashSet, double)} - Basic Constructor for the thread. {@link #run()} - Calls tick, then yields to main thread {@link #tick()} - Every dt it gets all the new offers from the actors, and then tries to match them. {@link #isViable(core.Offer, core.Offer)} - Method That ensures that two offers are viable with one another.
 * 
 * @author Brian Oluwo
 */
public class OfferChannel extends Thread {
	
	public Thread thread;
	protected HashSet<Actor> actors;
	protected LinkedBlockingQueue<Transaction> globalTransactions;
	protected LinkedBlockingQueue<Offer>[] offerArrays;
	private double dt;
	
	@SuppressWarnings("unchecked")
	public OfferChannel(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, double dt) {
		this.globalTransactions = globalTransactions;
		this.actors = actors;
		this.dt = dt;
		this.thread = new Thread(this);
		this.offerArrays = new LinkedBlockingQueue[values().length];
		for(int i = 0; i < values().length; i++) {
			this.offerArrays[i] = new LinkedBlockingQueue<Offer>();
		}
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
			try {
				Offer o = actor.getBestOffer();
				if(o == null) {
					System.err.println("No Offer Given, Moving On.");
					continue;
				}
				this.offerArrays[o.getCommodity1().ordinal()].put(o);
			} catch(InterruptedException | NullPointerException e) {
				e.printStackTrace();
			}
		}
		processOffers();
	}
	
	/**
	 * Processes the offers in the queue
	 */
	private void processOffers() {
		//Only need to go through values()-1 because last one will have been checked against everything already
		//Only need to check the ones that are greater than you
		for(int i = 0, offerArraysLength = this.offerArrays.length - 1; i < offerArraysLength; i++) {
			LinkedBlockingQueue<Offer> offerArray = this.offerArrays[i];
			if(offerArray == null) {
				continue;
			}
			for(Offer first : offerArray) {
				for(int j = i + 1; j < offerArraysLength + 1; j++) {
					LinkedBlockingQueue<Offer> secondOfferArray = this.offerArrays[j];
					if(secondOfferArray == null) {
						continue;
					}
					for(Offer second : secondOfferArray) {
						assert second != null : "Null Transaction offered.";
						if(isViable(first, second)) {
							Transaction t = new Transaction(first.getMinReceive(), first.getCommodity2(), second.getMinReceive(), first.getCommodity1(), first.getSender());
							Transaction q = new Transaction(second.getMinReceive(), second.getCommodity2(), first.getMinReceive(), second.getCommodity1(), second.getSender());
							if(t.getSender().acceptTransaction(t)) {
								if(q.getSender().acceptTransaction(q)) {
									t.getCommodity1().addTransaction(t);
									t.getCommodity2().addTransaction(q);
									try {
										this.globalTransactions.put(t);
										this.globalTransactions.put(q);
									} catch(InterruptedException e) {
										e.printStackTrace();
									}
									
									//Probably could use offerArray to avoid all the calls
									//but i don't remember the rules for this removal in
									//whether it is simply removing the pointer or removing
									//the object, But just to be safe
									//offerArray.remove(first);
									//secondOfferArray.remove(second);
									offerArrays[first.getCommodity1().ordinal()].remove(first);
									offerArrays[second.getCommodity1().ordinal()].remove(second);
									//this.offers.remove(first);
									//this.offers.remove(second);
									
									//System.out.println("Processed");
								} else {
									t.getSender().acceptTransaction(q);//Reverse their transaction
									//secondOfferArray.remove(second);
									offerArrays[second.getCommodity1().ordinal()].remove(second);
									//this.offers.remove(second);
								}
							} else {
								//offerArray.remove(first);
								offerArrays[first.getCommodity1().ordinal()].remove(first);
								//this.offers.remove(first);
							}
						}
					}
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
		return this.offerArrays[commodity.ordinal()].size();
	}
	
	/**
	 * Specific for between two commodities. Takes longer.
	 * 
	 * @param tradeAway - Trading Away Commodity
	 * @param tradeFor - Trading For Commodity
	 * @return - Number of Trades trading away tradeAway, for tradeFor
	 */
	public int getNumberOfOffers(Commodity tradeAway, Commodity tradeFor) {
		int count = 0;
		for(Offer offer : this.offerArrays[tradeAway.ordinal()]) {
			if(offer.getCommodity2().name().equals(tradeFor.name()))
				count++;
		}
		return count;
	}
}
