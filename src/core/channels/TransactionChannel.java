package core.channels;

import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;

/**
 * Transaction Thread class
 * {@link #TransactionChannel(LinkedBlockingQueue, Double)} - Basic Constructor for the thread. 
 * Takes in all it needs to create a transactionary environment.
 * {@link #run()}-Blocks until the queue is no longer empty then performs functions.
 */
public class TransactionChannel implements Runnable {

	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Transaction> transactions = null;
	final private long sysStartTime;
	private int marketIterations = 0;
	private double dt;


	/**
	 * This constructor sends in the global transaction queue, but says to evaluate transactions every dt.
	 * 
	 * @param queue - Global Transaction Queue
	 * @param evalNum - Size of each eval batch
	 * @param dt - How often the transaction queue should be evaluated.
	 */
	public TransactionChannel(LinkedBlockingQueue<Transaction> queue, double dt) {
		this.transactions = queue;
		this.sysStartTime = System.currentTimeMillis();
		this.marketIterations=0;
		this.dt = dt;
	}

	@Override
	/**
	 * Untested
	 */
	public void run() {
		if(System.currentTimeMillis() == this.sysStartTime + ((long)(this.marketIterations * this.dt * 1000))){
			process();
		}
		else{}
	}
	/**
	 * Untested.
	 */
	private void process(){
		for(Transaction t : this.transactions){
			for(Transaction q : this.transactions){
				if(t.equals(q.getReversedTransaction())){
					t.getSender().acceptTransaction(t);
					q.getSender().acceptTransaction(q);
					//TODO: Implement enum, so it can just be an iterative search for which commodity to add to.
					//What I'm about to write is just dummy code while I wait for an implementation.
					t.getCommodity1().addTransaction(t);
					t.getCommodity2().addTransaction(q);
				}
			}
		}
	}
	
}