package core.GUI;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * Holding class for a collection of GraphicalObjects
 * @author Sam Maynard
 *
 */
public abstract class Scene {
	
	protected LinkedList<GraphicalObject> graphicalObjects;
	protected Listener listener;
	
	/**
	 * constructor
	 * @param listener the listener to pass GraphicalObject's messages on to
	 */
	public Scene(Listener listener) {
		this.graphicalObjects = new LinkedList<GraphicalObject>();
		this.listener = listener;
	}
	
	/**
	 * draws each of the graphical objects
	 * @param g graphics to draw with
	 */
	public void drawSelf(Graphics2D g) {
		for(GraphicalObject graphicalObject : graphicalObjects)
			graphicalObject.drawSelf(g);
	}
	
	/**
	 * checks which graphical object was clicked, and passes the click onto it
	 * @param click the mouse click
	 */
	public void processClick(MouseEvent click){
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
	
	/**
	 * passes a keystroke onto every GraphicalObject
	 * @param keystroke the keystroke object
	 */
	public void processKeystroke(KeyEvent keystroke){
		for (GraphicalObject graphicalObject : this.graphicalObjects) {
            graphicalObject.keyPressed(keystroke);
        }
	}
	
}
