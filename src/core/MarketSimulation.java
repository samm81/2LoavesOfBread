package core;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class MarketSimulation extends Simulation {

	protected HashSet<Actor> actors;
	protected LinkedList<Commodity> commodities;

	public HashSet<Actor> getActors() {
		return actors;
	}

	public LinkedList<Commodity> getCommodities() {
		return commodities;
	}

	public MarketSimulation(double dt) {
		super(dt);
		actors = new HashSet<Actor>();
		commodities = new LinkedList<Commodity>();
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void addCommodity(Commodity commodity) {
		commodities.add(commodity);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void tick() {
		for(Actor actor : actors) {
			actor.reevaluateWeights();
		}

		for(Actor actor : actors) {
			actor.evaluateMarket();
		}

		Random r = new Random();
		int commodity1 = r.nextInt(commodities.size());
		int commodity2 = 0;
		do {
			commodity2 = r.nextInt(commodities.size());
		} while(commodity2 == commodity1);
		commodities.get(commodity1).addTransaction(new Transaction(r.nextInt(9) + 1, commodities.get(commodity1), r.nextInt(9) + 1, commodities.get(commodity2)));

	}

	public static class Bread extends Commodity {}

	public static class Fish extends Commodity {}

	public static class Watermelon extends Commodity {}

	public static class Pizza extends Commodity {}

}
