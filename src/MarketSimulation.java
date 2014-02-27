import java.awt.Color;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JFrame;


public class MarketSimulation extends Simulation {

	HashSet<Actor> actors;
	HashSet<Commodity> commodities;
	
	public static void main(String[] args) {
		MarketSimulation sim = new MarketSimulation(1);
		
		Fish fish = new Fish();
		Bread bread = new Bread();
		Random r = new Random();
		for(int i = 0;i<40;i++){
			bread.addTransaction(new Transaction(r.nextInt(10), fish, r.nextInt(10), bread));
		}
		
		sim.addCommodity(bread);
		sim.addCommodity(new Fish());
		sim.addCommodity(new Oranges());
		
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(350, 75, 900, 700);
		
		DoubleBufferedCanvas canvas = new MarketCanvas(40, sim);
		canvas.setBackground(Color.WHITE);
		f.add(canvas);
		f.setVisible(true);
		
		canvas.start();
	}
	
	public HashSet<Actor> getActors() {
		return actors;
	}

	public HashSet<Commodity> getCommodities() {
		return commodities;
	}

	public MarketSimulation(double dt) {
		super(dt);
		actors = new HashSet<Actor>();
		commodities = new HashSet<Commodity>();
	}
	
	public void addActor(Actor actor) {
		actors.add(actor);
	}
	
	public void addCommodity(Commodity commodity){
		commodities.add(commodity);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void tick() {
		for(Actor actor : actors){
			actor.reevaluateWeights();
		}
		
		for(Actor actor : actors){
			actor.evaluateMarket();
		}
	}
	
	public static class Bread extends Commodity {	}
	public static class Fish extends Commodity {	}
	public static class Oranges extends Commodity {	}

}
