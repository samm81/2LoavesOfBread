package core.commodities;

import java.awt.Color;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

import core.Transaction;

/**
 * Abstract class representing every object that can be traded.
 * 
 * @author Sam Maynard
 * 
 */
public abstract class Commodity {
	
	LinkedList<Transaction> transactions; // every transaction that has occured involving this commodity
	Hashtable<Class<? extends Commodity>, Ticker> tickers; // the tickers for the objects it trades for
	Hashtable<Class<? extends Commodity>, Double> mostRecentRatios; // the most recent trade ratio for each other commodity
	
	Color color; // the commoditie's color
	
	public Commodity(Color color) {
		transactions = new LinkedList<Transaction>();
		tickers = new Hashtable<Class<? extends Commodity>, Ticker>();
		mostRecentRatios = new Hashtable<Class<? extends Commodity>, Double>();
		
		this.color = color;
	}
	
	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}
	
	public Hashtable<Class<? extends Commodity>, Ticker> getTickers() {
		return tickers;
	}
	
	public Collection<Ticker> getTickerCollection() {
		return tickers.values();
	}
	
	public Hashtable<Class<? extends Commodity>, Double> getMostRecentRatios() {
		return this.mostRecentRatios;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Fills this commodities tickers with new tickers,
	 * one for every commodity given (besides itself)
	 * 
	 * @param commodities the commodities to create tickers from
	 * @param tickerMagnitude the number of transactions the ticker shows
	 */
	public void createTickersFromCommodities(LinkedList<Commodity> commodities, int tickerMagnitude) {
		for(Commodity commodity : commodities) {
			if(!commodity.getClass().equals(this.getClass())) {
				tickers.put(commodity.getClass(), new Ticker(tickerMagnitude, commodity.getColor()));
				mostRecentRatios.put(commodity.getClass(), 0d);
			}
		}
	}
	
	/**
	 * Inserts a transaction into the transactions list,
	 * and updates mostRecentRatios, which is used to update the
	 * ticker.
	 * 
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		if(!isOrderedProperly(transaction)) {
			transaction = transaction.getReversedTransaction();
		}
		transactions.add(transaction);
		
		Commodity tradeCommodity = transaction.getCommodity2();
		/*
		 * if(tickers.containsKey(tradeCommodity.getClass())) {
		 * tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		 * } else {
		 * tickers.put(tradeCommodity.getClass(), new Ticker(tickerMagnitude, randomColor()));
		 * tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		 * }
		 */
		mostRecentRatios.put(tradeCommodity.getClass(), transaction.getRatio());
		
	}
	
	/**
	 * Checks if a transaction has this classes commodity first
	 * 
	 * @param transaction the transaction to check
	 * @return true if it is in order, false otherwise
	 */
	private boolean isOrderedProperly(Transaction transaction) {
		return transaction.getCommodity1().getClass().equals(this.getClass());
	}
	
}
