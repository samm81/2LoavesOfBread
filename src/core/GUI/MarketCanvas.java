package core.GUI;

import core.MarketSimulation;
import core.Transaction;
import core.commodities.Commodity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import static java.awt.Color.*;

/**
 * The general container for all the games elements.
 *
 * @author Sam Maynard
 */
@SuppressWarnings ("serial")
public class MarketCanvas extends DoubleBufferedCanvas {

    protected MarketSimulation sim;

    protected LinkedBlockingQueue<GraphicalObject> graphicalObjects;

    protected TransparencyOverlay overlay;
    protected MakeOfferPopup makeOfferPopup;
    protected GoButton goButton;

    public MarketCanvas(int fps, MarketSimulation sim) {
        super(fps);
        this.sim = sim;
    }

    @Override
    void init() {
        Key key = new Key(0, 0, this.getWidth(), 40, this, this.sim.getCommodities());
        Inventory inventory = new Inventory(0, this.getHeight() - 150, this.getWidth(), 150, this, this.sim.getCommodities(), this.sim.getPlayer());
        MakeOfferButton makeOfferButton = new MakeOfferButton(this.getWidth() - 250, this.getHeight() - 100, 220, 50, this);
        addGraphicalObject(key);
        addGraphicalObject(inventory);
        addGraphicalObject(makeOfferButton);

        LinkedList<Graph> graphs = createGraphs(this.sim.getCommodities(), 200);
        for (Graph graph : graphs)
            addGraphicalObject(graph);

        Color color = new Color(1f, 1f, 1f, .8f);
        this.overlay = new TransparencyOverlay(this, color);

        int width = 800;
        int height = 100;
        int x = this.getWidth() / 2 - width / 2;
        int y = this.getHeight() / 2 - height / 2;
        this.makeOfferPopup = new MakeOfferPopup(x, y, width, height, this, this.sim.getCommodities());

        this.goButton = new GoButton(x + width - 95, y + 25, 75, 50, this);
    }

    /**
     * Adds a GraphicalObject to the list of GraphicalObjects to
     * be drawn.
     *
     * @param graphicalObject GraphicalObject to be added
     */
    private void addGraphicalObject(GraphicalObject graphicalObject) {
        if (this.graphicalObjects == null)
            this.graphicalObjects = new LinkedBlockingQueue<>();

        this.graphicalObjects.add(graphicalObject);
    }

    private void removeGraphicalObject(GraphicalObject graphicalObject) {
        if (this.graphicalObjects != null) {
            this.graphicalObjects.remove(graphicalObject);
        }
    }

    /**
     * generates the graphs from a list of commodities
     *
     * @param commodities the commodities to generate graphs from
     * @param graphHeight      the height of the graphs
     * @return LinkedList<Graph> of the graphs created
     */
    private LinkedList<Graph> createGraphs(LinkedList<Commodity> commodities, int graphHeight) {
        LinkedList<Graph> graphs = new LinkedList<>();

        int graphWidth = this.getWidth() / 2 - 5;
        int i = 0;
        for (Commodity commodity : commodities) {
            int x = (i % 2) * (this.getWidth() / 2 + 5);
            int y = 45 + (i / 2) * 203;
            graphs.add(new Graph(x, y, graphWidth, graphHeight, this, commodity));
            i++;
        }
        return graphs;
    }

    @Override
    void draw(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(BLACK);

        for (GraphicalObject graphicalObject : this.graphicalObjects)
            graphicalObject.drawSelf(g);

    }

    @Override
    protected void updateVars() {}

    @Override
    protected void processInputs() {
        if (this.mouseClicksWaiting()) {
            LinkedList<MouseEvent> clicks = this.flushMouseClickQueue();
            for (MouseEvent click : clicks) {
                int x = click.getX();
                int y = click.getY();
                GraphicalObject topObjectClicked = null;
                for (GraphicalObject graphicalObject : this.graphicalObjects) {
                    if (graphicalObject.pointInBounds(x, y))
                        topObjectClicked = graphicalObject;
                }
                if (topObjectClicked != null)
                    topObjectClicked.clicked(click);
            }
        }

        if (this.keyPressesWaiting()) {
            LinkedList<KeyEvent> keyPresses = this.flushKeyPressQueue();
            for (KeyEvent keyPress : keyPresses) {
                for (GraphicalObject graphicalObject : this.graphicalObjects) {
                    graphicalObject.keyPressed(keyPress);
                }
            }
        }
    }

    /**
     * Receives messages from the GraphicalObjects
     *
     * @param message message from a GraphicalObject
     */
    public void message(String message) {
        switch (message) {
            case "MakeOfferOverlay":
                addGraphicalObject(this.overlay);
                addGraphicalObject(this.makeOfferPopup);
                addGraphicalObject(this.goButton);
                break;
            case "CloseMakeOffer":
                removeGraphicalObject(this.makeOfferPopup);
                removeGraphicalObject(this.overlay);
                removeGraphicalObject(this.goButton);
                break;
            case "OfferMade":
                int volume1 = this.makeOfferPopup.getVolume1();
                int volume2 = this.makeOfferPopup.getVolume2();
                Commodity commodity1 = this.makeOfferPopup.getCommodity1();
                Commodity commodity2 = this.makeOfferPopup.getCommodity2();
                Transaction offer = new Transaction(volume1, commodity1, volume2, commodity2, this.sim.getPlayer());
                this.sim.getPlayer().setBestOffer(offer);
                this.message("CloseMakeOffer");
                break;
        }
    }

}
