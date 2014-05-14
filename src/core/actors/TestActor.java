package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * @author Brian Oluwo
 */
public class TestActor extends Actor {
	
	public TestActor(List<Commodity> list, LinkedBlockingQueue<Transaction> transactions) {
		super(list, transactions, new int[] { 5, 9, 5, 0}, new int[] { 9, 11, 10, 10000});
	}
}
