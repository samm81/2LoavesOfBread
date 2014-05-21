package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**Farmers are Actors that begin the game with
 * 4 Oxen, and 5 Watermelon
 * They desire: 30 loaves of bread, 10 fish, and 4 oxen.
 * @author Brian Oluwo
 */
public class Farmer extends Actor {
	
	public Farmer(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] { 0, 0, 4, 5 }, new int[] { 30, 10, 4, 0 });
	}
}
