package core.actors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import core.Offer;
import core.Transaction;
import core.channels.OfferChannel;
import core.commodities.Commodity;
/**
 * Player is the player actor, controlled by the player.
 * @author patrickcshan
 *
 */
public class Player extends Actor {
	
	Offer bestOffer;
	ConcurrentHashMap<Commodity, Integer> goalVolumes;
	/**
	 * Constructor for player
	 * @param commodities 
	 * the commodities in the system
	 * @param startingVolumes 
	 * the starting inventory
	 * @param goalVolumes 
	 * the goal of the player.
	 */
	public Player(List<Commodity> commodities, int[] startingVolumes, int[] goalVolumes) {
		this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
		this.goalVolumes = new ConcurrentHashMap<Commodity, Integer>(goalVolumes.length);
		int i = 0;
		for(Commodity commodity : commodities) {
			this.goalVolumes.put(commodity, goalVolumes[i]);
			i++;
		}
		
		i = 0;
		for(Commodity commodity : commodities) {
			this.volumes.put(commodity, startingVolumes[i]);
			i++;
		}
		bestOffer = null;
	}
	
	/**
	 * Get the hashmap that contains the player's inventory
	 * @return
	 */
	public ConcurrentHashMap<Commodity, Integer> getVolumes() {
		return this.volumes;
	}
	
	/**
	 * Get the hashmap that contains the player's goal inventory.
	 * @return
	 */
	public ConcurrentHashMap<Commodity, Integer> getGoalVolumes() {
		return this.goalVolumes;
	}
	
	/**
	 * Sets player's best offer.
	 * @param bestOffer
	 */
	public void setBestOffer(Offer bestOffer) {
		this.bestOffer = bestOffer;
	}
	
	@Override
	/**
	 * Getter for the best offer.
	 */
	public Offer getBestOffer() {
		return this.bestOffer;
	}
	
	@Override
	/**
	 * EvaluateMarket determines whether or not the player is still able to 
	 * make the offer he wants to make (In case his inventory changes based on
	 * view market). 
	 * @param offerChannel
	 */
	public void evaluateMarket(OfferChannel offerChannel) {}
	
	public boolean canMakeOffer(Transaction transaction){
		if(transaction == null)
			return false;
		Commodity commodity = transaction.getCommodity1();
		int volume = transaction.getVolume1();
		return volumes.get(commodity) >= volume;
	}

	/**
	 * Determines whether or not the player can make the offer.
	 * @param offer
	 * @return
	 */
	public boolean canMakeOffer(Offer offer) {
		if(offer == null || offer.getSender() != this)
			return false;
		Commodity commodity = offer.getCommodity1();
		int volume = offer.getMaxTradeVolume();
		return volumes.get(commodity) >= volume;
	}
	
	@Override
	/**
	 * Accepts transactions that satisfies both the player and the AI.
	 * @param t
	 */
	public boolean acceptTransaction(Transaction t) {
		//System.out.println("Player accepting transaction " + t);
		this.volumes.put(t.getCommodity1(), this.volumes.get(t.getCommodity1()) + t.getVolume1());
		this.volumes.put(t.getCommodity2(), this.volumes.get(t.getCommodity2()) - t.getVolume2());
		this.bestOffer = null;
		return true;
	}

	
}
