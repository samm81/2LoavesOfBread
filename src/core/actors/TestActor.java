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
		super(list, transactions, new int[] { 100, 100, 100, 100}, new int[] { 100, 90, 100, 110});
	}
}
