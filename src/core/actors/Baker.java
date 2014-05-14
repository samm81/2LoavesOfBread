package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;


public class Baker extends Actor {

	public Baker(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] {40, 0, 0, 0}, new int[] { 0, 20, 0, 20 });
	}
	
}
