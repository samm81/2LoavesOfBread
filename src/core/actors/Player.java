package core.actors;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

public class Player extends Actor {
	
	public Player(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		super(commodities, transaction);
	}
	
	Transaction bestOffer = null;
	
	public Transaction getBestOffer() {
		return bestOffer;
	}
	
	public void setBestOffer(Transaction bestOffer) {
		this.bestOffer = bestOffer;
	}
	
	@Override
	public void evaluateMarket() {}
	
}
