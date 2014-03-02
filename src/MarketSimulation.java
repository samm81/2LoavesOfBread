import java.awt.Color;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

public class MarketSimulation extends Simulation {

	HashSet<Actor> actors;
	LinkedList<Commodity> commodities;
	final static String [] comNames = {"Fish","Bread","Watermelon","Pizza"};
	public static void main(String[] args) {
		MarketSimulation sim = new MarketSimulation(1);
		for(int i=0; i < comNames.length;i++){
			sim.addCommodity(new Commodity(comNames[i]));
		}
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int width = 900;
		int height = 700;
		f.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2 - 50, 900, 700);

		DoubleBufferedCanvas canvas = new MarketCanvas(40, sim);
		canvas.setBackground(Color.WHITE);
		f.add(canvas);
		f.setVisible(true);

		canvas.start();
		sim.start();
	}

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

}
