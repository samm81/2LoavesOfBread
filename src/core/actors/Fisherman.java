package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;


public class Fisherman extends Actor {

	public Fisherman(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] {0, 40, 0, 0}, new int[] { 20, 0, 0, 20 });
	}
	
}
