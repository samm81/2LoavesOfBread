package core;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import core.commodities.Commodity;

public class Actor {
	
	protected LinkedList<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	
	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
	}
	
	public void reevaluateWeights() {
		// TODO Auto-generated method stub
	}
	
	public Transaction getBestOffer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void evaluateMarket() {
		// TODO Auto-generated method stub
		
	}
	
}
