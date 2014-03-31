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
public enum Commodity {
	Fish(Color.BLUE),
	Bread(Color.YELLOW.darker()),
	Watermelon(Color.green),
	Oxen(Color.RED);
	LinkedList<Transaction> transactions; // every transaction that has occured involving this commodity
	Hashtable<String, Ticker> tickers; // the tickers for the objects it trades for
	Hashtable<String, Double> mostRecentRatios; // the most recent trade ratio for each other commodity
	
	Color color; // the commoditie's color
	
	private Commodity(Color color) {
		transactions = new LinkedList<Transaction>();
		tickers = new Hashtable<String, Ticker>();
		mostRecentRatios = new Hashtable<String, Double>();
		
		this.color = color;
	}
	
	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}
	
	public Hashtable<String, Ticker> getTickers() {
		return tickers;
	}
	
	public Collection<Ticker> getTickerCollection() {
		return tickers.values();
	}
	
	public Hashtable<String, Double> getMostRecentRatios() {
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
			if(!commodity.name().equals(this.name())) {
				tickers.put(commodity.name(), new Ticker(tickerMagnitude, commodity.getColor()));
				mostRecentRatios.put(commodity.name(), 0d);
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
		mostRecentRatios.put(tradeCommodity.name(), transaction.getRatio());
		
	}
	
	/**
	 * Checks if a transaction has this classes commodity first
	 * @param transaction the transaction to check
	 * @return true if it is in order, false otherwise
	 */
	private boolean isOrderedProperly(Transaction transaction) {
		return transaction.getCommodity1().name().equals(this.name());
	}
	
}
