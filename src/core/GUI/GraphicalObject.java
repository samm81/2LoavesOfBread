package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;


abstract class GraphicalObject implements Clickable {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	Shape shape;
	
	protected final int outlineOffset = 3;
	
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
	
	/**
	 * Draws a rounded rectangle with a white inside and black border
	 * based on the shape of the GraphicalObject
	 * 
	 * @param g Graphics2D object to do the drawing with
	 */
	protected void drawOutline(Graphics2D g) {
		drawOutline(Color.WHITE, g);
	}
	
	/**
	 * Draws a rounded rectangle with a white inside and black border
	 * based on the shape of the GraphicalObject
	 * 
	 * @param backgroundColor background color to fill the outline with
	 * @param g Graphics2D object to do the drawing with
	 */
	protected void drawOutline(Color backgroundColor, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fill(shape);
		g.setColor(backgroundColor);
		g.fill(makeShape(x + outlineOffset, y + outlineOffset, width - outlineOffset*2, height - outlineOffset*2));
	}
	
	@Override
	public void clicked() {
		System.out.println(this.getClass().getSimpleName() + " clicked");
	}
	
}
