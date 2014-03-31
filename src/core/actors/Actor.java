package core.actors;

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
	protected LinkedBlockingQueue<Transaction> transactions; // list of global transactions
	protected ConcurrentHashMap<String, Integer> volumes;
	private final Integer startingVolumes = new Integer(3); 

	public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
		this.commodities = commodities;
		this.transactions = transaction;
		this.volumes= new ConcurrentHashMap<String, Integer>(this.commodities.size());
		for(Commodity s : this.commodities)
			this.volumes.put(s.name(),this.startingVolumes);

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
	public void submitTransction(int vol1, Commodity s1, int vol2, Commodity s2){
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
				new Integer(
						(int) 
						(this.volumes.get(t.getCommodity1().name()).intValue() //Null pointer thrown here because actor doesn't have volumes
								- t.getVolume1())));
		this.volumes.put(t.getCommodity2().name(), new Integer((int) (this.volumes.get(t.getCommodity2().name()).intValue() + t.getVolume2())));
	}
}