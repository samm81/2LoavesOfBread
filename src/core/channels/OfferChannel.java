package core.channels;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.actors.*;

/**
 * Transaction Thread class {@link #OfferChannel(LinkedBlockingQueue, Double)} - Basic Constructor for the thread.
 * Takes in all it needs to create a transactionary environment. {@link #run()}-Blocks until the queue is no longer empty then performs functions.
 */
public class OfferChannel implements Runnable {
	
	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Transaction> offers;
	protected HashSet<Actor> actors;
	final private long sysStartTime;
	private int marketIterations;
	private double dt;
	protected LinkedBlockingQueue<Transaction> globalTransactions;
	
	/**
	 * This constructor sends in the global transaction queue, but says to evaluate offers every dt.
	 * 
	 * @param globalTransactions
	 * @param dt
	 */
	public OfferChannel(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, double dt) {
		this.globalTransactions = globalTransactions;
		this.actors = actors;
		this.dt = dt;

		this.offers = new LinkedBlockingQueue<Transaction>();

		this.sysStartTime = System.currentTimeMillis();
		this.marketIterations = 0;
	}
	
	@Override
	public void run() {
		while(!(System.currentTimeMillis() == this.sysStartTime + ((long) (this.marketIterations * this.dt * 1000)))) {
			for(Actor actor : this.actors) {
				try {
					this.offers.put(actor.getBestOffer());
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			process();
		}
	}
	
	private void process(){
		System.err.println(offers.size());

		for(Transaction t : this.offers) {
			for(Transaction q : this.offers) {
				if(true) {//t.equals(q.getReversedTransaction()) && t.getState() == false && q.getState() == false){
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
			}
		}
	}
	
}
