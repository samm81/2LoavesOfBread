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
		super(list, transactions, new int[] { 100, 100, 1, 100}, new int[] { 20, 20, 200000, 20});
	}
}
