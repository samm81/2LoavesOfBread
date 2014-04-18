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
        super(commodities,transactions,new int[]{1, 2, 3, 5}, new int[]{10,100,210,120}, 1);
    }
}
