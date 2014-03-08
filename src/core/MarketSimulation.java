package core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import core.commodities.Commodity;

public class MarketSimulation extends Simulation {
	
	protected HashSet<Actor> actors;
	protected LinkedList<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	
	public MarketSimulation(double dt) {
		super(dt);
		actors = new HashSet<Actor>();
		commodities = new LinkedList<Commodity>();
		transactions = new LinkedBlockingQueue<Transaction>();
	}
	
	public LinkedBlockingQueue<Transaction> getTrans() {
		// TODO Auto-generated method stub
		return this.getTrans();
	}
	
	public HashSet<Actor> getActors() {
		return this.actors;
	}
	
	public LinkedList<Commodity> getCommodities() {
		return this.commodities;
	}
	
	public void addActor(Actor actor) {
		this.actors.add(actor);
	}
	
	public void addCommodity(Commodity commodity) {
		this.commodities.add(commodity);
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void tick() {
		for(Actor actor : this.actors) {
			actor.reevaluateWeights();
		}
		
		for(Actor actor : this.actors) {
			actor.evaluateMarket();
		}
		
		Random r = new Random();
		int commodity1 = r.nextInt(this.commodities.size());
		int commodity2 = 0;
		do {
			commodity2 = r.nextInt(this.commodities.size());
		} while(commodity2 == commodity1);
		this.commodities.get(commodity1).addTransaction(new Transaction(r.nextInt(9) + 1, this.commodities.get(commodity1), r.nextInt(9) + 1, commodities.get(commodity2)));
		
	}
	
}
