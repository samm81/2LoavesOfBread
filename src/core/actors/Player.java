package core.actors;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import core.Offer;
import core.Transaction;
import core.commodities.Commodity;

public class Player extends Actor {
	
	Offer bestOffer;
	
	public Player(List<Commodity> commodities, int[] startingVolumes) {
		this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
		int i = 0;
		for(Commodity commodity : commodities) {
			this.volumes.put(commodity, startingVolumes[i]);
			i++;
		}
		bestOffer = null;
	}
	
	public ConcurrentHashMap<Commodity, Integer> getVolumes() {
		return this.volumes;
	}
	
	public void setBestOffer(Offer bestOffer) {
		this.bestOffer = bestOffer;
	}
	
	@Override
	public Offer getBestOffer() {
		return this.bestOffer;
	}
	
	@Override
	public void evaluateMarket() {}
	
	@Override
	public boolean acceptTransaction(Transaction t) {
		if(this.volumes.get(t.commodity1) - t.volume1 > 0 && t.commodity1 != t.commodity2 && t.volume1 != 0 && t.volume2 != 0) {
			//System.out.println("Player accepting transaction " + t);
			this.bestOffer = null;
		}
		return super.acceptTransaction(t);
	}
	
}
