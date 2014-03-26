package core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import core.commodities.Commodity;

/**
 * Abstract class the represents every player.
 * @author Sam "Fabulous Hands" Maynard
 */
public abstract class Actor {
	
	protected LinkedList<Commodity> commodities; // list of global commodities
	protected LinkedBlockingQueue<Transaction> transactions; // list of global transactions
	protected ConcurrentHashMap<Commodity, Integer> volumes;
	private final Integer startingVolumes = new Integer(3); 
	
	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
		this.volumes= new ConcurrentHashMap<Commodity, Integer>(this.commodities.size());
		Iterator<Commodity> i = this.commodities.iterator();
		while(i.hasNext()){
			this.volumes.put(i.next(),new Integer(this.startingVolumes));
		}
	}
	// patrick:
	// actor figures out what they want the most right now, and places an open offer
	// for how much they are willing to trade for it
	public abstract Transaction getBestOffer();
	// patrick:
	// actor should look at their goods, their wants, and the market
	// then reevaluate how much they are willing to trade for each object
	public abstract void evaluateMarket();
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.
	public void submitTransction(Commodity s1, Commodity s2, int vol1, int vol2) throws InterruptedException{
		transactions.put(new Transaction(vol1, s1, vol2, s2,this));
	}
	//TODO: Ensure they can actually afford to lose the volume of commodity they are trading.
	//TODO: Problem occurs if on the second check the Actor doesn't have the required volume of commodities. 
	//Then we need to negate the transaction on both ends. Can be fixed by utilizing the complex transaction method
	//Or making the transaction an exchange between two actors so that they can ensure volumes and quickly send
	//Signals to whether or not something has occured, but this is getting away from the concept of the simple trans
	public void acceptTransaction(Transaction t){
		//Update Correlating volumes.
		this.volumes.put(t.getCommodity1(), new Integer((int) (this.volumes.get(t.getCommodity1()).intValue() + t.getVolume1())));
		this.volumes.put(t.getCommodity2(), new Integer((int) (this.volumes.get(t.getCommodity2()).intValue() + t.getVolume2())));
	}
}