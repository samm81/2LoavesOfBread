package core.channels;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import core.*;
/**
 * @author Brian Oluwo
 * Transaction Thread class
 * ########################
 * {@link #TransactionChannel(BlockingQueue, HashSet)} - Basic Constructor for the thread. Takes in all it needs to create a transactionary environment.
 * {@link #run()}-Blocks until the queue is no longer empty then performs functions.
 *  
 */
public class TransactionChannel implements Runnable{
	//Use ArrayBlockingQueue so that when array is full it blocks automatically
	protected ArrayBlockingQueue<Transaction> transactions = null;
	//How many transactions to evaluate at a time
	final private int evalSize = 10;
	public TransactionChannel(BlockingQueue<Transaction> queue ) {
		this.transactions = (ArrayBlockingQueue<Transaction>) queue;
	}


	public void run() {

		if(!transactions.isEmpty() && transactions.size() >= evalSize){
			try{
				//Go ahead and do your thing. Acquire the lock and perform desired functions
				synchronized(transactions){
					for(Transaction t : transactions){//Actually should create an [10], dequeue 10 into array and then process
						t.setState(Transaction.STATE_PENDING);
						/*Operations Flow:
						 * 1. Lock their resources-Confirm both parties have the resources available for trade.-IF
						 *  * 1a. Deduct and add the agreed upon amounts
						 *  * 1b. Set state to Accepted, Unlock resources Continue
						 * 2. If the values don't checkout, set state to invalid, Unlock resources and continue-ELSE
						 */
						System.out.println(t.toString());
						t.setState(Transaction.STATE_ACCEPTED);
					}
				}
			}finally{
				//Release Lock on your resources
			}
		}
		else{
			//transactions.wait();
		}

	}
}