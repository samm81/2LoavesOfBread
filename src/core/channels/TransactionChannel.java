package core.channels;

import java.util.HashSet;
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

	protected BlockingQueue<Transaction> transactions = null;
	protected HashSet<Actor> actors;
	//How many transactions to evaluate at a time
	final private int evalSize = 10;
	public TransactionChannel(BlockingQueue<Transaction> queue,HashSet<Actor> set) {
		this.transactions = queue;
		this.actors = set;
	}


	public void run() {
		try {
			if(!transactions.isEmpty() && transactions.size() >= evalSize){
				//Go ahead and do your thing. Acquire the lock and perform desired functions
				synchronized(transactions){
					for(Transaction t : transactions){
						t.setState(t.STATE_PENDING);
						/*This is where we will do all our operations
						 * Like update the global market
						 */
						System.out.println(t.toString());
						t.setState(t.STATE_ACCEPTED);
					}
				}
			}
			else{
				transactions.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			//Release Lock on your resources
		}
	}
}