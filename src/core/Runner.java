package core;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import core.GUI.MarketCanvas;
import core.actors.Player;
import core.channels.OfferChannel;
import core.commodities.Commodity;

/**
 * The runner for the game. Gets the frame, the canvas, and the
 * simulation up and going. Holds all adjustable constants.
 * 
 * @author Sam "Fabulous Hands" Maynard
 * 
 */
public class Runner {

	static int tickerMagnitude = 150;
	static int width = 900;
	static int height = 700;
	static final double dt = .1d;

	/**
	 * Sets up the MarketSimulation and JFrame
	 */
	public static void main(String[] args) {
		OfferChannel offers = new OfferChannel(new LinkedBlockingQueue<Transaction>(), 
				new LinkedBlockingQueue<Transaction>(), dt*4);
		MarketSimulation sim = new MarketSimulation(offers.getGlobalList(),dt);
		//Creates the transaction thread that evaluates offers, every dt.
		Thread transactions = new Thread(offers); 
		// ok I agree, there's got to be a better way to do this
		// enum maybe?
		sim.addCommodity(Commodity.Fish);
		sim.addCommodity(Commodity.Bread);
		sim.addCommodity(Commodity.Watermelon);
		sim.addCommodity(Commodity.Oxen);

		
//		sim.addActor(new Actor(sim.getCommodities(), sim.getTransactions()));
//		sim.addActor(new Actor(sim.getCommodities(), sim.getTransactions()));
//		sim.addActor(new Actor(sim.getCommodities(), sim.getTransactions()));
//		sim.addActor(new Actor(sim.getCommodities(), sim.getTransactions()));
//		sim.addActor(new Actor(sim.getCommodities(), sim.getTransactions()));

		Player p = new Player(sim.getCommodities(),offers.getOfferList());
		sim.addActor(p);
		Player p2 = new Player(sim.getCommodities(),sim.getTransactions());
		sim.addActor(p2);
		sim.createTickers(tickerMagnitude); // required

		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLayout(null);
		f.getContentPane().setBackground(Color.DARK_GRAY);

		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		f.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2 - 50, width, height);

		MarketCanvas canvas = new MarketCanvas(60, sim);
		canvas.setBackground(Color.DARK_GRAY);
		// these two are the height and the width that the frame takes up with it's surrounding bar
		// found by trial and error
		final int frameWidthPadding = 6;
		final int frameHeightPadding = 29;
		canvas.setBounds(10, 10, f.getWidth() - frameWidthPadding - 20, f.getHeight() - frameHeightPadding - 20);
		f.add(canvas);
		f.setVisible(true);		
		canvas.start();
		sim.start();
		transactions.setDaemon(true);
		transactions.start();

	}
}
