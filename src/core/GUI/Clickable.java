package core.GUI;



/**
 * interface for clickable objects
 * @author Sam Maynard
 *
 */
interface Clickable {
	
	boolean pointInBounds(int x, int y);
	
	void clicked();
	
}
