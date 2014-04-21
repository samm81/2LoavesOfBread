package core.GUI;

import static java.awt.Color.BLACK;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.MarketSimulation;
import core.Offer;
import core.GUI.MakeOfferScene.MakeOfferPopup;
import core.GUI.MakeOfferScene.MakeOfferScene;
import core.GUI.TickerScene.GoButton;
import core.GUI.TickerScene.TickerScene;
import core.GUI.ViewMarketScene.ViewMarketPopup;
import core.GUI.ViewMarketScene.ViewMarketScene;
import core.commodities.Commodity;

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

    @Override
    void init() {
    	int width = this.getWidth();
    	int height = this.getHeight();
        tickerScene = new TickerScene(width, height, sim.getCommodities(), sim.getPlayer());
        makeOfferScene = new MakeOfferScene(width, height, sim.getCommodities(), tickerScene);
        viewMarketScene = new ViewMarketScene(width, height, sim.getOfferChannel(), tickerScene);
        
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
        if (this.mouseClicksWaiting()) {
            LinkedList<MouseEvent> clicks = this.flushMouseClickQueue();
            for (MouseEvent click : clicks) {
                selectedScene.processClick(click);
            }
        }

        if (this.keyPressesWaiting()) {
            LinkedList<KeyEvent> keystrokes = this.flushKeystrokeQueue();
            for (KeyEvent keystroke : keystrokes) {
                selectedScene.processKeystroke(keystroke);
            }
        }
    }

	@Override
	public void hear(String message) {
		 switch (message) {
		 }
	}

}
