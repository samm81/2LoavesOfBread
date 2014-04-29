package core.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import core.MarketSimulation;
import core.Offer;
import core.Transaction;
import core.GUI.MakeOfferScene.MakeOfferScene;
import core.GUI.TickerScene.TickerScene;
import core.GUI.ViewMarketScene.ViewMarketScene;

/**
 * The general container for all the games elements.
 * 
 * @author Sam Maynard
 */
@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {
	
	protected MarketSimulation sim;
	
	TickerScene tickerScene;
	MakeOfferScene makeOfferScene;
	ViewMarketScene viewMarketScene;
	
	LinkedList<Scene> scenes;
	
	Scene selectedScene;
	
	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}
	
	private void setSelectedScene(Scene scene) {
		selectedScene = scene;
	}
	
	@Override
	void init() {
		scenes = new LinkedList<Scene>();
		
		int width = this.getWidth();
		int height = this.getHeight();
		tickerScene = new TickerScene(width, height, sim.getCommodities(), sim.getPlayer(), sim, this);
		makeOfferScene = new MakeOfferScene(width, height, sim.getCommodities(), tickerScene, this);
		viewMarketScene = new ViewMarketScene(width, height, sim.getOfferChannel(), sim.getPlayer(), tickerScene, this);
		
		scenes.add(tickerScene);
		scenes.add(makeOfferScene);
		scenes.add(viewMarketScene);
		
		selectedScene = tickerScene;
	}
	
	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		selectedScene.drawSelf(g);
	}
	
	@Override
	protected void updateVars() {}
	
	@Override
	protected void processInputs() {
		if(this.mouseClicksWaiting()) {
			LinkedList<MouseEvent> clicks = this.flushMouseClickQueue();
			for(MouseEvent click : clicks) {
				selectedScene.processClick(click);
			}
		}
		
		if(this.keyPressesWaiting()) {
			LinkedList<KeyEvent> keystrokes = this.flushKeystrokeQueue();
			for(KeyEvent keystroke : keystrokes) {
				selectedScene.processKeystroke(keystroke);
			}
		}
	}
	
	@Override
	public void hear(String message, Object sender) {
		switch(message) {
		case "TickerScene":
			setSelectedScene(tickerScene);
			break;
		case "MakeOffer":
			setSelectedScene(makeOfferScene);
			break;
		case "ViewMarket":
			setSelectedScene(viewMarketScene);
			break;
		case "OfferMade":
			Transaction submittedTransaction = makeOfferScene.getSubmittedTransaction();
			if(submittedTransaction != null){
				Offer offer = new Offer(submittedTransaction, sim.getPlayer());
				sim.getPlayer().setBestOffer(offer);
				this.hear("TickerScene", this);
			}
			break;
		default:
			System.out.println(message);
			break;
		}
	}

}
