package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Brian Oluwo
 */
public class Farmer extends Actor {
    public Farmer(List<Commodity> commodities,LinkedBlockingQueue<Transaction> transactions) {
        super(commodities,transactions,new int[]{25,25,25,25}, new int[]{100,100,100,100}, 1);
    }
}
