package core.channels;

import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;

/**
 * Transaction Thread class
 * {@link #OfferChannel(LinkedBlockingQueue, Double)} - Basic Constructor for the thread. 
 * Takes in all it needs to create a transactionary environment.
 * {@link #run()}-Blocks until the queue is no longer empty then performs functions.
 */
public class OfferChannel implements Runnable {

	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Transaction> offers = null;
	final private long sysStartTime;
	private int marketIterations = 0;
	private double dt;
	protected LinkedBlockingQueue<Transaction> globalTransactions = null;

	/**
	 * This constructor sends in the global transaction queue, but says to evaluate offers every dt.
	 * 
	 * @param queue - Global Transaction Queue
	 * @param evalNum - Size of each eval batch
	 * @param dt - How often the transaction queue should be evaluated.
	 */
	public OfferChannel(LinkedBlockingQueue<Transaction> queue, LinkedBlockingQueue<Transaction> globalTransactions, double dt) {
		this.offers = queue;
		this.sysStartTime = System.currentTimeMillis();
		this.marketIterations=0;
		this.dt = dt;
		this.globalTransactions = globalTransactions;
	}
	public LinkedBlockingQueue<Transaction> getOfferList(){
		return this.offers;
	}
	public LinkedBlockingQueue<Transaction> getGlobalList(){
		return this.globalTransactions;
	}
	@Override
	/**
	 * TODO: Change to thread that runs through all the actors and calls get best offer. 
	 * This returns a transaction which is submitted to the transaction queue.
	 * TODO: Give Offers access to the actors, and make it a hashmap so i can use the actors as keys.
	 */
	public void run() {
		while(!(System.currentTimeMillis() == this.sysStartTime + ((long)(this.marketIterations * this.dt * 1000)))){
			process();
		}
	}
	/**
	 * Untested.
	 */
	private void process(){
		for(Transaction t : this.offers){
			for(Transaction q : this.offers){
				if(t.equals(q.getReversedTransaction()) && t.getState() == false && q.getState() == false){
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
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}