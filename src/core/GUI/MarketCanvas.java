package core.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import core.Game;
import core.MarketSimulation;
import core.GUI.CommodityCrashScene.CommodityCrashScene;
import core.GUI.EndGameScene.EndGameScene;
import core.GUI.MakeOfferScene.MakeOfferScene;
import core.GUI.TickerScene.TickerScene;
import core.GUI.TitleScene.TitleScene;
import core.GUI.ViewMarketScene.ViewMarketScene;
import core.GUI.WonGameScene.WonGameScene;
import core.commodities.Commodity;

/**
 * The general container for all the games elements.
 * 
 * @author Sam Maynard
 */
@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {
	
	protected MarketSimulation sim;
	private Game game;
	
	TitleScene titleScene;
	TickerScene tickerScene;
	MakeOfferScene makeOfferScene;
	ViewMarketScene viewMarketScene;
	EndGameScene endGameScene;
	CommodityCrashScene commodityCrashScene;
	WonGameScene wonGameScene;
	
	LinkedList<Scene> scenes;
	
	Scene selectedScene;
	
	public MarketCanvas(int fps, MarketSimulation sim, Game game) {
		super(fps);
		this.sim = sim;
		this.game = game;
	}
	
	public void setMarketSimulation(MarketSimulation sim) {
		this.sim = sim;
	}
	
	private void setSelectedScene(Scene scene) {
		selectedScene = scene;
	}
	
	
	public void reinit() {
		init();
	}
	
	@Override
	void init() {
		scenes = new LinkedList<Scene>();
		
		int width = this.getWidth();
		int height = this.getHeight();
		titleScene = new TitleScene(width, height, this);
		tickerScene = new TickerScene(width, height, sim.getCommodities(), sim.getPlayer(), sim, this);
		makeOfferScene = new MakeOfferScene(width, height, sim.getCommodities(), sim.getPlayer(), tickerScene, this);
		viewMarketScene = new ViewMarketScene(width, height, sim.getOfferChannel(), sim.getPlayer(), tickerScene, this);
		endGameScene = new EndGameScene(this);
		commodityCrashScene = new CommodityCrashScene(width, height, Commodity.Bread, tickerScene, this);
		wonGameScene = new WonGameScene(width, height, this);
		
		scenes.add(titleScene);
		scenes.add(tickerScene);
		scenes.add(makeOfferScene);
		scenes.add(viewMarketScene);
		scenes.add(endGameScene);
		scenes.add(commodityCrashScene);
		
		selectedScene = titleScene;
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
		case "Play":
			this.hear("TickerScene", sender);
			game.play();
			break;
		case "TickerScene":
			setSelectedScene(tickerScene);
			break;
		case "MakeOffer":
			setSelectedScene(makeOfferScene);
			break;
		case "ViewMarket":
			setSelectedScene(viewMarketScene);
			break;
		case "GameLost":
			setSelectedScene(endGameScene);
			break;
		case "GameWon":
			setSelectedScene(wonGameScene);
			break;
		case "CommodityCrash":
			commodityCrashScene = new CommodityCrashScene(getWidth(), getHeight(), (Commodity)sender, selectedScene, this);
			setSelectedScene(commodityCrashScene);
			break;
		default:
			System.out.println(message);
			break;
		}
	}

}
