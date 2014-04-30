package core.actors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import core.Offer;
import core.Transaction;
import core.channels.OfferChannel;
import core.commodities.Commodity;

public class Player extends Actor {
	
	Offer bestOffer;
	ConcurrentHashMap<Commodity, Integer> goalVolumes;
	
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
	
	public ConcurrentHashMap<Commodity, Integer> getVolumes() {
		return this.volumes;
	}
	public ConcurrentHashMap<Commodity, Integer> getGoalVolumes() {
		return this.goalVolumes;
	}
	
	public void setBestOffer(Offer bestOffer) {
		this.bestOffer = bestOffer;
	}
	
	@Override
	public Offer getBestOffer() {
		return this.bestOffer;
	}
	
	
	@Override
	public void evaluateMarket(OfferChannel offerChannel) {}
	
	@Override
	public boolean acceptTransaction(Transaction t) {
		//if(this.volumes.get(t.commodity1) - t.volume1 > 0 && t.commodity1 != t.commodity2 && t.volume1 != 0 && t.volume2 != 0) {
			System.out.println("Player accepting transaction " + t);
			this.volumes.put(t.getCommodity1(), this.volumes.get(t.getCommodity1()) + t.getVolume1());
			this.volumes.put(t.getCommodity2(), this.volumes.get(t.getCommodity2()) - t.getVolume2());
			this.bestOffer = null;
		//}
		return true;
	}
	
}
