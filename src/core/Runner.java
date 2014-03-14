package core;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;

import core.GUI.MarketCanvas;
import core.commodities.Bread;
import core.commodities.Fish;
import core.commodities.Oxen;
import core.commodities.Watermelon;

// import core.channels.TransactionChannel;

public class Runner {
	
	static int tickerMagnitude = 150;
	
	public static void main(String[] args) {
		MarketSimulation sim = new MarketSimulation(0.1);
		
		//TransactionChannel trans = new TransactionChannel(sim.getTrans());
		//Thread transactions = new Thread(trans);
		
		Fish fish = new Fish(Color.BLUE);
		Bread bread = new Bread(Color.YELLOW.darker());
		Watermelon watermelon = new Watermelon(Color.GREEN);
		Oxen oxen = new Oxen(Color.RED);
		
		sim.addCommodity(fish);
		sim.addCommodity(bread);
		sim.addCommodity(watermelon);
		sim.addCommodity(oxen);
		sim.createTickers(tickerMagnitude);
		
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int width = 900;
		int height = 700;
		f.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2 - 50, 900, 700);
		
		MarketCanvas canvas = new MarketCanvas(60, sim);
		canvas.setBackground(Color.DARK_GRAY);
		f.add(canvas);
		f.setVisible(true);
		
		canvas.start();
		sim.start();
		//transactions.start();
	}
}
