package core;

import static java.awt.Color.DARK_GRAY;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Toolkit;

import javax.swing.JFrame;

import core.GUI.MarketCanvas;

/**
 * The runner for the game. Gets the frame, the canvas, and the game going.
 *
 * @author Sam Maynard
 */
public class Runner {
	
	static int width = 900;
	static int height = 700;
	
	/**
	 * Sets Up Market Canvas and Game
	 * @param args - Command Line Args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame("Two Loaves of Bread");
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLayout(null);
		f.getContentPane().setBackground(DARK_GRAY);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		f.setBounds(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2 - 50, width, height);
		
		Game game = new Game();
		MarketCanvas canvas = game.getMarketCanvas();
		// height and the width that the frame takes up with it's surrounding bar, found by trial and error
		final int frameWidthPadding = 6;
		final int frameHeightPadding = 29;
		canvas.setBounds(10, 10, f.getWidth() - frameWidthPadding - 20, f.getHeight() - frameHeightPadding - 20);
		canvas.setBackground(DARK_GRAY);
		
		f.add(canvas);
		canvas.start();

		f.setVisible(true);
	}
	
}
