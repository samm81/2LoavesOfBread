package core.GUI;

import java.awt.event.MouseEvent;


/**
 * interface for clickable objects
 *
 * @author Sam Maynard
 */
public interface Clickable {

    boolean pointInBounds(int x, int y);

    void clicked(MouseEvent e);

}
