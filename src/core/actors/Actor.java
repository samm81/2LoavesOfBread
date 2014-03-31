package core.actors;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

/**
 * Abstract class the represents every player.
 * @author Sam "Fabulous Hands" Maynard
 */
public abstract class Actor {

	protected LinkedList<Commodity> commodities; // list of global commodities
	protected LinkedBlockingQueue<Transaction> transactions; // list of global offers
	protected ConcurrentHashMap<String, Integer> volumes;
	private final Integer startingVolumes = new Integer(3); 
	protected double[][] exchangematrix; //actor's own personal exchange rate
	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
		this.volumes= new ConcurrentHashMap<String, Integer>(this.commodities.size());
		for(Commodity s : this.commodities)
			this.volumes.put(s.name(),this.startingVolumes);
		this.exchangematrix = new double[commodities.size()][commodities.size()];
	}
	//For MVP: Selects a random thing and then picks an offer that they can actually make.
	//If they cannot afford making any offers it picks another random offer.
	public void getBestOffer()
	{
		System.out.println("Running BestOffer()");
		int want = (int) (Math.random()*this.commodities.size()); //item wanted
		int tradedaway = -1; //item to be traded for want

		int[] inven = new int[commodities.size()];
		for(int i = 0; i < inven.length; i++)
		{
			inven[i] = volumes.get(commodities.get(i));
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

		//this.submitTransaction(vol1, commodities.get(tradedaway), vol2, commodities.get(want)  );
	}
	// patrick:
	// actor should look at their goods, their wants, and the market
	// then reevaluate how much they are willing to trade for each object

	//This method will simply average the exchange rate and the new ratio for MVP. Then add/subtract a random amount.
	public void evaluateMarket(){
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
		getBestOffer();
	}
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.
	public void submitTransaction(int vol1, Commodity s1, int vol2, Commodity s2){
		try {
			transactions.put(new Transaction(vol1, s1, vol2, s2,this));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.

	public void acceptTransaction(Transaction t){
		//Update Correlating volumes.
		this.volumes.put(t.getCommodity1().name(), 
				new Integer((int)(this.volumes.get(t.getCommodity1().name()).intValue() //Null pointer thrown here because actor doesn't have volumes
						- t.getVolume1())));
		this.volumes.put(t.getCommodity2().name(), new Integer((int) (this.volumes.get(t.getCommodity2().name()).intValue() + t.getVolume2())));
	}
}