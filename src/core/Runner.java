package core;

import static java.awt.Color.DARK_GRAY;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Toolkit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import core.GUI.MarketCanvas;
import core.actors.Actor;
import core.actors.Farmer;
import core.actors.Merchant;
import core.actors.Player;
import core.channels.OfferChannel;
import core.commodities.Commodity;

/**
 * The runner for the game. Gets the frame, the canvas, and the
 * simulation up and going. Holds all adjustable constants.
 *
 * @author Sam "Fabulous Hands" Maynard
 */
public class Runner {
	
	static final double dt = .1d;
	static final double offerDT = dt;
	static final int numActors = 400;
	static int tickerMagnitude = 30;
	static int width = 900;
	static int height = 700;
	
	/**
	 * Sets Up Market Simulation, Canvas, and OfferChannel
	 * @param args - Command Line Args
	 */
	public static void main(String[] args) {
		LinkedList<Commodity> commodities = new LinkedList<Commodity>();
		for(Commodity item : Commodity.values())
			commodities.add(item);
		
		for(Commodity commodity : commodities)
			commodity.createTickersFromCommodities(commodities, tickerMagnitude);
		
		LinkedBlockingQueue<Transaction> transactions = new LinkedBlockingQueue<Transaction>();
		
		Player player = new Player(commodities, new int[] { 2, 0, 0, 0, 0, 0 });
		
		HashSet<Actor> actors = new HashSet<Actor>();
		for(int i = 0; i < numActors; i++) {
			Random r = new Random();
			if(r.nextBoolean())
				actors.add(new Farmer(commodities, transactions));
			else
				actors.add(new Merchant(commodities, transactions));
		}
		actors.add(player);
		
		//Creates the transaction thread that evaluates offers, every offerDT.
		OfferChannel offerChannel = new OfferChannel(transactions, actors, offerDT, actors.size());
		offerChannel.setDaemon(true);
		
		MarketSimulation sim = new MarketSimulation(commodities, transactions, player, actors, offerChannel, dt);
		
		MarketCanvas canvas = new MarketCanvas(60, sim);
		
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLayout(null);
		f.getContentPane().setBackground(DARK_GRAY);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		f.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2 - 50, width, height);
		canvas.setBackground(DARK_GRAY);
		// these two are the height and the width that the frame takes up with it's surrounding bar
		// found by trial and error
		final int frameWidthPadding = 6;
		final int frameHeightPadding = 29;
		canvas.setBounds(10, 10, f.getWidth() - frameWidthPadding - 20, f.getHeight() - frameHeightPadding - 20);
		f.add(canvas);
		f.setVisible(true);
		
		canvas.start();
		sim.start();
		offerChannel.start();
	}
}
