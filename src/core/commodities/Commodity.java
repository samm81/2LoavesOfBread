package core.commodities;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import core.Transaction;

/**
 * Abstract class representing every object that can be traded.
 * @author Sam Maynard
 */
public enum Commodity {
	Bread(Color.YELLOW.darker()),
	Fish(Color.BLUE),
	Oxen(Color.RED),
	Watermelon(Color.GREEN);

	LinkedList<Transaction> transactions; // every transaction that has occured involving this commodity
	HashMap<String, Ticker> tickers; // the tickers for the objects it trades for
	HashMap<String, Double> mostRecentRatios; // the most recent trade ratio for each other commodity
	Color color; // the commodity's color
	
	private Commodity(Color color) {
		this.transactions = new LinkedList<Transaction>();
		this.tickers = new HashMap<String, Ticker>();
		this.mostRecentRatios = new HashMap<String, Double>();
		this.color = color;
	}

	public LinkedList<Transaction> getTransactions() {
		return this.transactions;
	}

	public HashMap<String, Ticker> getTickers() {
		return this.tickers;
	}

	public Collection<Ticker> getTickerCollection() {
		return this.tickers.values();
	}

	public HashMap<String, Double> getMostRecentRatios() {
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
	 * @param tickerMagnitude the number of offers the ticker shows
	 */
	public void createTickersFromCommodities(LinkedList<Commodity> commodities, int tickerMagnitude) {
		for(Commodity commodity : commodities) {
			if(!commodity.name().equals(this.name())) {
				this.tickers.put(commodity.name(), new Ticker(tickerMagnitude, commodity.getColor()));
				this.mostRecentRatios.put(commodity.name(), 5d);
			}
		}
	}

	/**
	 * Inserts a transaction into the offers list,
	 * and updates mostRecentRatios, which is used to update the
	 * ticker.
	 * 
	 * @param transaction
	 */
	public void addTransaction(Transaction transaction) {
		if(!isOrderedProperly(transaction)) {
			transaction = transaction.getReversedTransaction();
		}
		this.transactions.add(transaction);

		Commodity tradeCommodity = transaction.getCommodity2();
		/*
		 * if(tickers.containsKey(tradeCommodity.getClass())) {
		 * tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		 * } else {
		 * tickers.put(tradeCommodity.getClass(), new Ticker(tickerMagnitude, randomColor()));
		 * tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		 * }
		 */
		this.mostRecentRatios.put(tradeCommodity.name(), transaction.getRatio());

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
