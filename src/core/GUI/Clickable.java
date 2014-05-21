package core.GUI;

import java.awt.event.MouseEvent;


/**
 * interface for clickable objects
 *
 * @author Sam Maynard
 */
public interface Clickable {

	/**
	 * checks if the point is contained within the object
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return true if the point is in bounds, false otherwise
	 */
    boolean pointInBounds(int x, int y);

    /**
     * method to specify how the object will behave when it is clicked
     * @param e the mouse click object
     */
    void clicked(MouseEvent e);

}
