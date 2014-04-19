package core.actors;

import java.util.concurrent.ConcurrentHashMap;

import core.Offer;
import core.commodities.Commodity;

public class Player extends Actor {

	Offer bestOffer;
	
	public Player(int[] startingVolumes) {
		this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
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
	
}
