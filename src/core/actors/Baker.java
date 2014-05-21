package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * Baker is a form of actor. 
 * It starts with 40 bread and nothing else
 * Bakers want 20 Fish and 20 Watermelons.
 * @author patrickcshan
 *
 */
public class Baker extends Actor {

	/**
	 * Baker Constructor
	 * @param commodities
	 * @param transactions
	 */
	public Baker(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions) {
		super(commodities, transactions, new int[] {40, 0, 0, 0}, new int[] { 0, 20, 0, 20 });
	}
	
}
