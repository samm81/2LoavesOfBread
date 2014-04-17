package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Brian Oluwo
 */
public class Merchant extends Actor {
    public Merchant(LinkedList<Commodity> commodities,LinkedBlockingQueue<Transaction> transactions) {
        super(commodities,transactions,new int[]{4, 4, 4, 4}, new double[]{4, 4, 4, 4});
    }
}
