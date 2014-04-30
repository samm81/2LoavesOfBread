package core.GUI;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public abstract class Scene {
	
	protected LinkedList<GraphicalObject> graphicalObjects;
	protected Listener listener;
	
	public Scene(Listener listener) {
		this.graphicalObjects = new LinkedList<GraphicalObject>();
		this.listener = listener;
	}
	
	public void drawSelf(Graphics2D g) {
		for(GraphicalObject graphicalObject : graphicalObjects)
			graphicalObject.drawSelf(g);
	}
	
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
	
	public void processKeystroke(KeyEvent keystroke){
		for (GraphicalObject graphicalObject : this.graphicalObjects) {
            graphicalObject.keyPressed(keystroke);
        }
	}
	
}
