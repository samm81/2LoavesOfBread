package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * Merchant is an actor that begins the game with 4 of each commodity (except 1 watermelon) and wants 20 of each other commodity.
 * @author Brian Oluwo
 */
public class Merchant extends Actor {
	
	public Merchant(List<Commodity> list, LinkedBlockingQueue<Transaction> transactions) {
		super(list, transactions, new int[] { 4, 4, 4, 1 }, new int[] { 20, 20, 20, 20 });
	}
}
