package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * @author Brian Oluwo
 */
public class Farmer extends Actor {
	
	public Farmer(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] { 0, 0, 4, 5 }, new int[] { 30, 10, 4, 0 });
	}
}
