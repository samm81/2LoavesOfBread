package core.actors;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * Abstract class the represents every player.
 * @author Sam "Fabulous Hands" Maynard
 */
public class Actor {
	
	protected LinkedList<Commodity> commodities; // list of global commodities (Can be used to get the exchange rate)
	protected LinkedBlockingQueue<Transaction> transactions; // list of global transactions
	protected ConcurrentHashMap<Commodity, Integer> volumes;
	private final Integer startingVolumes = new Integer(3); 
	
	
	protected double[][] exchangematrix; //actor's own personal exchange rate
	protected double[] wantmatrix; //what the actor wants and what they are willing to trade for.
	
	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
		this.volumes= new ConcurrentHashMap<Commodity, Integer>(this.commodities.size());
		this.exchangematrix = new double[commodities.size()][commodities.size()];
		
		
		Iterator<Commodity> i = this.commodities.iterator();
		while(i.hasNext()){
			Commodity a = i.next();
			this.volumes.put(a,new Integer(this.startingVolumes));
		}
	/*	i = this.commodities.iterator();
		int col = 0;
		while(i.hasNext()){
			Commodity a = i.next();
			Hashtable<String, Double> exchangerate = a.getMostRecentRatios();
			
			for(int row = 0; row < exchangematrix.length; row++)
			{
				if(row != col)
					exchangematrix[row][col] = exchangerate.get(commodities.get(row).name());
				else
					exchangematrix[row][col] = 1;
			}
			
			col++;
		}*/
		
		//random values to start with
		for(int row = 0; row < exchangematrix.length; row++)
			for(int col = 0; col < exchangematrix[row].length; col++)
			{
				exchangematrix[row][col] = Math.random();
			}
		
	}
	// patrick:
	// actor figures out what they want the most right now, and places an open offer
	// for how much they are willing to trade for it
	
	//For MVP: Selects a random thing and then picks an offer that they can actually make.
	//If they cannot afford making any offers it picks another random offer.
	public Transaction getBestOffer()
	{
		System.out.println("Running BestOffer()");
		int want = (int) (Math.random()*this.commodities.size()); //item wanted
		int tradedaway = -1; //item to be traded for want
		
		int[] inven = new int[this.commodities.size()];
		for(int i = 0; i < inven.length; i++)
		{
			inven[i] = volumes.get(this.commodities.get(i));
		}
		

		for(int i = 0; i < exchangematrix[want].length; i++)
		{
			if(tradedaway == -1)
				tradedaway = i;
			else
			{
				if(inven[i]/exchangematrix[want][i] > inven[tradedaway]/exchangematrix[want][tradedaway])
					tradedaway = i;
			}
		}
		
		int vol1 = (int) Math.ceil(inven[tradedaway]/2);
		int vol2 = (int) (vol1 * exchangematrix[want][tradedaway]);
		
		return new Transaction(vol1, this.commodities.get(tradedaway), vol2, this.commodities.get(want), this);
	}
	
	// patrick:
	// actor should look at their goods, their wants, and the market
	// then reevaluate how much they are willing to trade for each object
	
	//This method will simply average the exchange rate and the new ratio for MVP. Then add/subtract a random amount.
	public void evaluateMarket()
	{
		System.out.println("EVALUATING MARKETS! :D");
		Iterator<Commodity> i = this.commodities.iterator();
		int col = 0;
		while(i.hasNext()){
			Commodity a = i.next();
			Hashtable<String, Double> exchangerate = a.getMostRecentRatios();
			
			for(int row = 0; row < exchangematrix.length; row++)
			{
				if(exchangerate.get(a) == null)
					continue;
				else if(row != col)
					exchangematrix[row][col] = Math.abs((exchangematrix[row][col] + exchangerate.get(a.name()) / 2) + Math.random());
				else
					exchangematrix[row][col] = 1;
			}
			
			col++;
		}
	}
	
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.
	//Patrick: Done
	public void submitTransaction(Commodity s1, Commodity s2, int vol1, int vol2) throws InterruptedException{
		if(this.volumes.get(s1) - vol1 > 0)
			transactions.put(new Transaction(vol1, s1, vol2, s2,this));
	}
	
	public void submitTransaction(Transaction t) throws InterruptedException
	{
		if(this.volumes.get(t.commodity1) - t.getVolume1() > 0)
			transactions.put(t);
	}
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.
	//Patrick: Done

	public void acceptTransaction(Transaction t){
		//Update Correlating volumes.
		if(this.volumes.get(t.commodity1.name()) - t.volume1 > 0)
		{
		this.volumes.put(t.getCommodity1(), new Integer((int) (this.volumes.get(t.getCommodity1()).intValue() + t.getVolume1())));
		this.volumes.put(t.getCommodity2(), new Integer((int) (this.volumes.get(t.getCommodity2()).intValue() + t.getVolume2())));
		}
	}
}


/*
 * This is code that will pick an offer in the list that best matches the wants and needs.
 * It does not account for whether or not they can make that transaction though.
 * 
 * 		Transaction t = null;

Iterator<Transaction> i = transactions.iterator();
while(i.hasNext())
{
	if(t == null)
	{
		t = i.next();
	}
	else
	{
		Transaction t2 = i.next();
		double currRatio = t.getRatio();
		double newRatio = t2.getRatio();
		t = (newRatio > currRatio)?t2:t;
		
	}
}

if(t != null)
{
	acceptTransaction(t);
}
 */