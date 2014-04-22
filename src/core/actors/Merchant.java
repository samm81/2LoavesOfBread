package core.actors;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * @author Brian Oluwo
 */
public class Merchant extends Actor {
    public Merchant(List<Commodity> list, LinkedBlockingQueue<Transaction> transactions) {
        super(list,transactions,new int[]{4, 4, 4, 4}, new int[]{10,10,10,10}, 0.25);
    }
}
