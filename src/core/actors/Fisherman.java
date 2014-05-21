package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * Fisherman is an actor that begins the game with 40 fish.
 * A Fisherman desires 20 loaves of bread and 20 watermelon.
 * @author patrickcshan
 *
 */
public class Fisherman extends Actor {
	/**
	 * Fisherman Actor Constructor
	 * @param commodities
	 * @param transactions
	 */
	public Fisherman(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] {0, 40, 0, 0}, new int[] { 20, 0, 0, 20 });
	}
	
}
