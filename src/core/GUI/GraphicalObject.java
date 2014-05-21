package core.GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.awt.Color.BLACK;

public abstract class GraphicalObject implements Clickable {
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	Shape shape;
	
	/**
	 * constructor
	 * @param x x position of upper left corner
	 * @param y y position of upper left corner
	 * @param width width of the object
	 * @param height height of the object
	 */
	public GraphicalObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shape = makeShape(x, y, width, height);
	}
	
	/**
	 * Returns the specific shape that this GraphicalObject has
	 *
	 * @param x x coordinate of the shape
	 * @param y y coordinate of the shape
	 * @param width width of the shape
	 * @param height height of the shape
	 * @return Shape shape of this particular GraphicalObject
	 */
	protected abstract Shape makeShape(int x, int y, int width, int height);
	
	/**
	 * Draws the object
	 *
	 * @param g Graphics2D object to draw the object with
	 */
	public abstract void drawSelf(Graphics2D g);
	
	@Override
	public boolean pointInBounds(int x, int y) {
		return shape.contains(x, y);
	}
	
	/**
	 * Draws an outline of the graphical object
	 * @param backgroundColor the color to fill in the outline with
	 * @param outlineOffset the thickness of the outline
	 * @param g graphics object to draw with
	 */
	protected void drawOutline(Color backgroundColor, int outlineOffset, Graphics2D g) {
		g.setColor(BLACK);
		g.fill(shape);
		g.setColor(backgroundColor);
		g.fill(makeShape(x + outlineOffset, y + outlineOffset, width - outlineOffset * 2, height - outlineOffset * 2));
	}
	
	protected void drawOutline(Color backgroundColor, Graphics2D g) {
		drawOutline(backgroundColor, 3, g);
	}
	
	protected void drawOutline(Graphics2D g) {
		drawOutline(Color.WHITE, g);//new Color(0.17254902f, 0.17254902f, 0.17254902f, 1.0f), g);
	}
	
	@Override
	public void clicked(MouseEvent click) {}
	
	/**
	 * Allows the GraphicalObject to manipulate keyboard input, if it wants
	 * @param keystroke the KeyEvent of a key press
	 */
	public void keyPressed(KeyEvent keystroke) {}
	
}
