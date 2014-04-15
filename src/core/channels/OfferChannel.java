package core.channels;

import core.Offer;
import core.Transaction;
import core.actors.Actor;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * {@link #OfferChannel(java.util.concurrent.LinkedBlockingQueue, java.util.HashSet, double)} - Basic Constructor for the thread. {@link #run()} - Calls tick, then yields to main thread {@link #tick()} - Every dt it gets all the new offers from the actors, and then tries to match them. {@link #isViable(core.Offer, core.Offer)} - Method That ensures that two offers are viable with one another.
 * 
 * @author Brian Oluwo
 */
public class OfferChannel extends Thread {
	
	public Thread thread;
	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Offer> offers = new LinkedBlockingQueue<>();
	protected HashSet<Actor> actors;
	protected LinkedBlockingQueue<Transaction> globalTransactions;
	private double dt;
	
	public OfferChannel(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, double dt) {
		this.globalTransactions = globalTransactions;
		this.actors = actors;
		this.dt = dt;
		this.thread = new Thread(this);
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
	
	private void tick() {
		for(Actor actor : this.actors) {
			try {
				this.offers.put(actor.getBestOffer());
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		processOffers();
	}
	
	private void processOffers() {
		for(Offer first : this.offers) {
			assert first != null : "Null Transaction offered.";
			for(Offer second : this.offers) {
				assert second != null : "Null Transaction offered.";
				if(isViable(first, second)) {
					Transaction t = new Transaction(first.getMinReceive(), first.getCommodity1(), second.getMinReceive(), first.getCommodity2(), first.getSender());
					Transaction q = new Transaction(second.getMinReceive(), second.getCommodity1(), first.getMinReceive(), second.getCommodity2(), second.getSender());
					t.getSender().acceptTransaction(t);
					q.getSender().acceptTransaction(q);
					t.getCommodity1().addTransaction(t);
					t.getCommodity2().addTransaction(q);
					this.offers.remove(first);
					this.offers.remove(second);
					try {
						this.globalTransactions.put(t);
						this.globalTransactions.put(q);
					} catch(InterruptedException e) {
						e.printStackTrace();
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
		// A wants 2 fish for x(2*1.5)bread @ a rate of 1.5, B wants 3 bread for x(3*.6667) fish
		if(!first.getCommodity1().name().equals(second.getCommodity2().name()) || !first.getCommodity2().name().equals(second.getCommodity1().name()))
			return false;
		if(first.getMinReceive() > second.getMaxTradeVolume() || second.getMinReceive() > first.getMaxTradeVolume())
			return false;
		
		return true;
	}
}
