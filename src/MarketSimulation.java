import java.awt.Color;
import java.util.HashSet;

import javax.swing.JFrame;


public class MarketSimulation extends Simulation {

	HashSet<Actor> actors;
	HashSet<Commodity> commodities;
	
	public static void main(String[] args) {
		MarketSimulation sim = new MarketSimulation(1);
		
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(350, 75, 900, 700);
		
		DoubleBufferedCanvas canvas = new MarketCanvas(40, sim);
		canvas.setBackground(Color.WHITE);
		f.add(canvas);
		f.setVisible(true);
		
		canvas.start();
	}
	
	public MarketSimulation(double dt) {
		super(dt);
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

}
