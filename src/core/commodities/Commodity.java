package core.commodities;
import java.awt.Color;
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

import core.Transaction;

public abstract class Commodity {
	
	LinkedList<Transaction> transactions;
	Hashtable<Class<?>, Ticker> tickers;
	Hashtable<Class<?>, Double> mostRecentRatios;
	
	int tickerMagnitude = 100;
	
	public Commodity() {
		transactions = new LinkedList<Transaction>();
		tickers = new Hashtable<Class<?>, Ticker>();
		mostRecentRatios = new Hashtable<Class<?>, Double>();
	}
	
	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}
	
	public Hashtable<Class<?>, Ticker> getTickers() {
		return tickers;
	}
	
	public Collection<Ticker> getTickerCollection() {
		return tickers.values();
	}
	
	
	public Hashtable<Class<?>, Double> getMostRecentRatios() {
		return this.mostRecentRatios;
	}
	
	public void createTickersFromCommodities(LinkedList<Commodity> commodities){
		for(Commodity commodity : commodities){
			tickers.put(commodity.getClass(), new Ticker(tickerMagnitude, randomColor()));
			mostRecentRatios.put(commodity.getClass(), 0d);
		}
	}
	
	private Color randomColor() {
		Random r = new Random();
		return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
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
