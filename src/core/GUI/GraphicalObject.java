package core.GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

abstract class GraphicalObject implements Clickable {
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	DoubleBufferedCanvas canvas;
	Shape shape;
	
	public GraphicalObject(int x, int y, int width, int height, DoubleBufferedCanvas canvas) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shape = makeShape(x, y, width, height);
		this.canvas = canvas;
	}
	
	/**
	 * Returns the specific shape that this GraphicalObject has
	 * 
	 * @param x x coordinate of the shape
	 * @param y y coordinate of the shape
	 * @param width width of the shape
	 * @param height height of the shape
	 * @return Shape of this particular GraphicalObject
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
	
	protected void drawOutline(Color backgroundColor, int outlineOffset, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fill(shape);
		g.setColor(backgroundColor);
		g.fill(makeShape(x + outlineOffset, y + outlineOffset, width - outlineOffset * 2, height - outlineOffset * 2));
	}
	
	/**
	 * Draws a rounded rectangle with a white inside and black border
	 * based on the shape of the GraphicalObject
	 * 
	 * @param backgroundColor background color to fill the outline with
	 * @param g Graphics2D object to do the drawing with
	 */
	protected void drawOutline(Color backgroundColor, Graphics2D g) {
		drawOutline(backgroundColor, 3, g);
	}
	
	/**
	 * Draws a rounded rectangle with a white inside and black border
	 * based on the shape of the GraphicalObject
	 * 
	 * @param g Graphics2D object to do the drawing with
	 */
	protected void drawOutline(Graphics2D g) {
		drawOutline(Color.WHITE, g);
	}
	
	@Override
	public void clicked(MouseEvent e) {}
	
	/**
	 * Allows the GraphicalObject to manipulate keyboard input
	 * 
	 * @param e the KeyEvent of a key press
	 */
	public void keyPressed(KeyEvent e) {}
	
}
