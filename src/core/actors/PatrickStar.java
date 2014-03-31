package core.actors;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

public class PatrickStar extends Actor {

	public PatrickStar(LinkedList<Commodity> commodities,
			LinkedBlockingQueue<Transaction> transaction) {
		super(commodities, transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Transaction getBestOffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evaluateMarket() {
		// TODO Auto-generated method stub
		
	}

}
