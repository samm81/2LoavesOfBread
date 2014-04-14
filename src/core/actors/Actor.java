package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract class the represents every player.
 *
 * @author Patrick Shan
 */
public enum Actor{
	FARMER(new int[]{1,1,1,5}, new double[]{0,1,2,3}, .5, .10),
	MERCHANT(new int[]{4,4,4,4}, new double[]{4,4,4,4}, .85, .35);
	
	
	LinkedList<Commodity> commodities;
	LinkedBlockingQueue<Transaction> transactions;
	double[][] exchangematrix;
	double[] wantmatrix;
	ConcurrentHashMap<Commodity, Integer> volumes;
	
	int[] initialvals;
	Transaction bestOffer;
	
	//these variables determine how the actor's exchange matrix changes.
	//market savviness determines how quickly the actor's exchange matrix reaches the real price
	//risk determines how much the actor is willing to increase their sales. (If someone bought it for $5, let me try to sell if for $6)
	double risk;
	double marketSavvy;
	
	
	
	/**
	 * Constructor
	 * @author Patrick Shan
	 * @param startingVolumes
	 * @param priorities
	 */
	private Actor(int[] startingVolumes, double[] priorities, double marketSavvy, double risk)
	{
		this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
		this.exchangematrix = new double[startingVolumes.length][startingVolumes.length];
		this.initialvals = startingVolumes;
		this.wantmatrix = priorities;
		this.risk = risk;
		this.marketSavvy = marketSavvy;
		
	}
	
public void initialize(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions){
		this.commodities = commodities;
		this.transactions = transactions;
		//exchange matrix setup.
		for(int row = 0; row < exchangematrix.length; row++) {
			for(int col = 0; col < exchangematrix[row].length; col++) {
				exchangematrix[row][col] = Math.random();
			}
			}
				int i = 0;
		//this.commodities has the same order as Commodity.values();
		//sets up the starting inventory
		for(Commodity s : this.commodities)
		{
			this.volumes.put(s, initialvals[i]);
			i++;
		}
				
	}	
	/**
	 * Get Best Offer returns a transaction that the actor will submit based on
	 * an actor's want matrix and exchange matrix.
	 * @author Patrick Shan
	 * @return Transaction that the actor will submit.
	 */
	public Transaction getBestOffer(){
		int want = (int) (Math.random() * this.commodities.size()); //item wanted
		int tradedaway = -1; //item to be traded for want
		int[] inven = new int[this.commodities.size()];
		for(int i = 0; i < inven.length; i++) {
			inven[i] = volumes.get(this.commodities.get(i));
		}
		for(int i = 0; i < exchangematrix[want].length; i++) {
			if(tradedaway == -1)
				tradedaway = i;
			else {
				if(inven[i] / exchangematrix[want][i] > inven[tradedaway] / exchangematrix[want][tradedaway])
					tradedaway = i;
			}
		}
		int vol1 = (int) Math.ceil(inven[tradedaway] / 2);
		int vol2 = (int) (vol1 * exchangematrix[want][tradedaway]);
		bestOffer = new Transaction(vol1, this.commodities.get(tradedaway), vol2, this.commodities.get(want), this);
		return bestOffer;
	}
	
	/**
	 * EvaluateMarket is how an actor will change the values in the market's exchange rates
	 * for various goods. It represents the ability of people to recognize changing prices.
	 * @author Patrick Shan
	 */
	public void evaluateMarket() {
		int col;
		int row = 0;
		//change the exchange matrix.
		//Exchange Matrix is set up so
		//row and column 0 is one commmodity, row, column 1 is one commodity ... etc.
		//this for loop shows the movement of markets
			for(Commodity x : Commodity.values())
			{
				col = 0;
				for(Commodity y : Commodity.values())
				{
					if(y == x)
						exchangematrix[row][col] = 1;
					else if(x.getMostRecentRatios().get(y) != null)
					{
						System.out.println(x.name() + " " + y.name() +"\n");
						System.out.println(row + " " + col);
						System.out.println(x.getMostRecentRatios().get(y));
						exchangematrix[row][col] = exchangematrix[row][col] +  (exchangematrix[row][col] - x.getMostRecentRatios().get(y)) * marketSavvy + (Math.random() * risk);
					}
					else
					{
						exchangematrix[row][col] = exchangematrix[row][col] + ((Math.random() - 0.5) * 2 * risk);
					}
					col++;
				}
				row++;
			}
	}
	
	/**
	 * AcceptTransaction changes actor inventories as they perform transaction t.
	 * @param t the transaction to be performed
	 */
	public void acceptTransaction(Transaction t) {
		if(this.volumes.get(t.commodity1) - t.volume1 > 0) {
			this.volumes.put(t.getCommodity1(), new Integer((int) (this.volumes.get(t.getCommodity1()).intValue() + t.getVolume1())));
			this.volumes.put(t.getCommodity2(), new Integer((int) (this.volumes.get(t.getCommodity2()).intValue() + t.getVolume2())));
		}
	}
}

