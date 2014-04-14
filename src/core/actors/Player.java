package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Player {

    Transaction bestOffer = null;

    public Player(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {

    }

    /*	public Transaction getBestOffer() {
            return bestOffer;
        }
    */
    public void setBestOffer(Transaction bestOffer) {
        this.bestOffer = bestOffer;
    }

}
