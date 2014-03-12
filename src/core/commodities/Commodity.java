package core.commodities;
import java.awt.Color;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;

import core.Transaction;

public abstract class Commodity {
	
	LinkedList<Transaction> transactions;
	Hashtable<Class<? extends Commodity>, Ticker> tickers;
	Hashtable<Class<? extends Commodity>, Double> mostRecentRatios;
	
	int tickerMagnitude = 100;
	
	Color color;
	
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
	
	public void createTickersFromCommodities(LinkedList<Commodity> commodities){
		for(Commodity commodity : commodities){
			tickers.put(commodity.getClass(), new Ticker(tickerMagnitude, commodity.getColor()));
			mostRecentRatios.put(commodity.getClass(), 0d);
		}
	}
	
	
	public Color getColor() {
		return this.color;
	}
	
	public void addTransaction(Transaction transaction) {
		if(!isOrderedProperly(transaction)) {
			transaction = transaction.getReversedTransaction();
		}
		transactions.add(transaction);
		
		Commodity tradeCommodity = transaction.getCommodity2();
		/*
		if(tickers.containsKey(tradeCommodity.getClass())) {
			tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		} else {
			tickers.put(tradeCommodity.getClass(), new Ticker(tickerMagnitude, randomColor()));
			tickers.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		}
		*/
		mostRecentRatios.put(tradeCommodity.getClass(), transaction.getRatio());
		
	}
	
	private boolean isOrderedProperly(Transaction transaction) {
		return transaction.getCommodity1().getClass().equals(this.getClass());
	}
	
}
