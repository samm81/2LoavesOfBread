package core.channels;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;

/**
 * Transaction Thread class
 * {@link #TransactionChannel(BlockingQueue, HashSet)} - Basic Constructor for the thread. 
 * Takes in all it needs to create a transactionary environment.
 * {@link #run()}-Blocks until the queue is no longer empty then performs functions.
 * 
 * @author Brian Oluwo
 * 
 * 
 */
public class TransactionChannel implements Runnable {

	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected LinkedBlockingQueue<Transaction> transactions = null;
	//How many transactions to evaluate at a time
	final private int evalSize;//obsolete
	final private boolean batchMode;//obsolete
	final private long sysStartTime;
	private int marketIterations = 0;
	private double dt;
	private ArrayBlockingQueue<Transaction> evalArray;//obsolete

	/**
	 * This constructor sends in the global transaction queue, 
	 * but says to evaluate Transactions as soon as they arrive.
	 * 
	 * @param queue- Global Transactions Queue
	 */
	public TransactionChannel(LinkedBlockingQueue<Transaction> queue,double dt) {
		this.transactions = queue;
		this.batchMode = false;
		this.evalSize = 0;
		this.evalArray = new ArrayBlockingQueue<Transaction>(this.evalSize);
		this.sysStartTime = System.currentTimeMillis();
		this.marketIterations=0;
		this.marketIterations=0;
		this.dt = dt;
	}

	/**
	 * This constructor sends in the global transaction queue, but says to evaluate in batches size evalNum.
	 * 
	 * @param queue - Global Transaction Queue
	 * @param evalNum - Size of each eval batch
	 * @param dt 
	 */
	public TransactionChannel(LinkedBlockingQueue<Transaction> queue, int evalNum, double dt) {
		this.transactions = queue;
		this.batchMode = true;
		this.evalSize = evalNum > 1 ? evalNum : 10;
		this.evalArray = new ArrayBlockingQueue<Transaction>(this.evalSize);
		this.sysStartTime = System.currentTimeMillis();
		this.marketIterations=0;
		this.dt = dt;
	}

	@Override
	public void run() {
		if(System.currentTimeMillis() == this.sysStartTime + ((long)(this.marketIterations * this.dt)/1000)){
			process();
		}
		else{}
		/*if(!this.batchMode) {
			try {
				//If we want to keep the block processing we would drainTo an array 
				//and send that off for processing
				Transaction t = transactions.take();
				process(t);
			} catch(InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if(this.transactions.size() >= this.evalSize) {
				synchronized (this.transactions) {
					this.transactions.drainTo(this.evalArray, this.evalSize);
				}
				process(this.evalArray);
				//For checking reverse: corned hash
			}

		}*/
	}

	private synchronized void process(ArrayBlockingQueue<Transaction> a) {
		for(Transaction t : a) {
			t.setState(Transaction.STATE_PENDING);
			/*
			 * Operations Flow:
			 * 1. Lock their resources-Confirm both parties have the resources available for trade.-IF
			 * * 1a. Deduct and add the agreed upon amounts
			 * * 1b. Set state to Accepted, Unlock resources Continue
			 * 2. If the values don't checkout, set state to invalid, Unlock resources and continue-ELSE
			 */
			System.out.println(t.toString());
			t.setState(Transaction.STATE_ACCEPTED);
			//TODO: Set transactionProcessed boolean to true for each actor.
		}
		a.clear();
	}
	private void process(){
		for(Transaction t  : this.transactions){
			for(Transaction q : this.transactions){
				if(t.equals(q.getReversedTransaction())){
					//Send actor the sign that they have an offer.
				}
			}
		}
	}
	private void process(Transaction t) {
		t.setState(Transaction.STATE_PENDING);
		/*
		 * Operations Flow:
		 * 1. Lock their resources-Confirm both parties have the resources available for trade.-IF
		 * * 1a. Deduct and add the agreed upon amounts
		 * * 1b. Set state to Accepted, Unlock resources Continue
		 * 2. If the values don't checkout, set state to invalid, Unlock resources and continue-ELSE
		 */
		System.out.println(t.toString());
		t.setState(Transaction.STATE_ACCEPTED);

	}
}