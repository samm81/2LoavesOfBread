package core.commodities;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.GUI.Ticker;

/**
 * Abstract class representing every object that can be traded.
 *
 * @author Sam Maynard
 */
public enum Commodity {
	Bread(Color.YELLOW.darker()), Fish(Color.BLUE), Oxen(Color.RED), Watermelon(Color.GREEN);//,
	
	//Radish(Color.MAGENTA),
	//Olive(Color.GREEN.darker().darker());
	
	LinkedBlockingQueue<Transaction> transactions; // every transaction that has occurred involving this commodity
	HashMap<Commodity, Ticker> tickers; // the tickers for the objects it trades for
	HashMap<Commodity, LinkedBlockingQueue<Double>> mostRecentRatios; // the most recent trade ratio for each other commodity
	Color color; // the commodity's color
	
	HashMap<Commodity, Double> lastAverages; // for each other commodity, what average rate has it been trading at
	
	/**
	 * constructor
	 * @param color the color of the commodity
	 */
	private Commodity(Color color) {
		this.transactions = new LinkedBlockingQueue<>();
		this.tickers = new HashMap<>();
		this.mostRecentRatios = new HashMap<Commodity, LinkedBlockingQueue<Double>>();
		this.color = color;
		
		this.lastAverages = new HashMap<Commodity, Double>();
	}
	
	public LinkedBlockingQueue<Transaction> getTransactions() {
		return this.transactions;
	}
	
	public HashMap<Commodity, Ticker> getTickers() {
		return this.tickers;
	}
	
	public Collection<Ticker> getTickerCollection() {
		return this.tickers.values();
	}
	
	public LinkedBlockingQueue<Double> getMostRecentRatios(Commodity commodity) {
		return this.mostRecentRatios.get(commodity);
	}
	
	public void clearMostRecentRatios(Commodity commodity) {
		this.lastAverages.put(commodity, getAverageRatio(commodity));
		this.mostRecentRatios.get(commodity).clear();
	}
	
	/**
	 * returns the average ratio from this commodity to a commodity
	 * @param commodity commodity to check ratio for
	 * @return the average ratio from this commodity to a commodity
	 */
	public double getAverageRatio(Commodity commodity) {
		double average = 0;
		LinkedBlockingQueue<Double> datum = getMostRecentRatios(commodity);
		if(datum.size() == 0)
			return -1;
		for(double data : datum)
			average += data;
		average /= datum.size();
		return average;
	}
	
	/**
	 * returns the average that happened before the most recent one
	 * @param commodity  commodity to check ratio for
	 * @return the average that happened before the most recent one
	 */
	public double getLastAverage(Commodity commodity) {
		return this.lastAverages.get(commodity);
	}
	
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Fills this commodities tickers with new tickers,
	 * one for every commodity given (besides itself)
	 * @param commodities the commodities to create tickers from
	 * @param tickerMagnitude the number of offers the ticker shows
	 */
	public void createTickersFromCommodities(List<Commodity> commodities, int tickerMagnitude) {
		for(Commodity commodity : commodities) {
			if(!commodity.name().equals(this.name())) {
				this.tickers.put(commodity, new Ticker(tickerMagnitude, 6, commodity.getColor()));
				this.mostRecentRatios.put(commodity, new LinkedBlockingQueue<Double>());
				this.lastAverages.put(commodity, 1d);
			}
		}
	}
	
	/**
	 * Inserts a transaction into the offers list,
	 * and updates mostRecentRatios, which is used to update the
	 * ticker.
	 *
	 * @param transaction- Passed in Transaction
	 */
	public void addTransaction(Transaction transaction) {
		if(!isOrderedProperly(transaction)) {
			transaction = transaction.reverse();
		}
		this.transactions.add(transaction);
		
		Commodity tradeCommodity = transaction.getCommodity2();
		try {
			this.mostRecentRatios.get(tradeCommodity).add(transaction.getRatio());
		} catch(NullPointerException e) {}
	}
	
	/**
	 * Checks if a transaction has this classes commodity first
	 *
	 * @param transaction the transaction to check
	 * @return true if it is in order, false otherwise
	 */
	private boolean isOrderedProperly(Transaction transaction) {
		return transaction.getCommodity1().name().equals(this.name());
	}
}
