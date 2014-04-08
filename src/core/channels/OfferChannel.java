package core.channels;

import core.Transaction;
import core.actors.Actor;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Transaction Thread class {@link #OfferChannel(java.util.concurrent.LinkedBlockingQueue, java.util.HashSet, double)} - Basic Constructor for the thread. {@link #run()} - Calls tick, then yields to main thread
 */
public class OfferChannel extends Thread {
	
	public Thread thread;
	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Transaction> offers = new LinkedBlockingQueue<>();
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
					Thread.sleep((long) (dt * 1000));
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
		process();
		
	}
	
	private void process() {
		System.err.println(offers.size());
		for(Transaction t : this.offers)
			for(Transaction q : this.offers)
				if(t.equals(q.getReversedTransaction()) && !t.getState() && !q.getState()) {
					t.setState(true);
					q.setState(true);
					t.getSender().acceptTransaction(t);
					q.getSender().acceptTransaction(q);
					t.getCommodity1().addTransaction(t);
					t.getCommodity2().addTransaction(q);
					this.offers.remove(t);
					this.offers.remove(q);
					try {
						this.globalTransactions.put(t);
						this.globalTransactions.put(q);
					} catch(InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		System.err.printf("Now there are: %d%n", offers.size());
	}
	
}
