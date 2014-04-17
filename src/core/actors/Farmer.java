package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Brian Oluwo
 */
public class Farmer extends Actor {
    public Farmer(LinkedList<Commodity> commodities,LinkedBlockingQueue<Transaction> transactions) {
        super(commodities,transactions,new int[]{1, 1, 1, 5}, new double[]{0, 1, 2, 3});
    }
}
