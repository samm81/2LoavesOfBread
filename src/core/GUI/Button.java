package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;

/**
 * An abstract button class
 * @author Sam Maynard
 *
 */
public abstract class Button extends GraphicalObject {
	
	protected Color backgroundColor;
	protected String text;
	protected String message;
	protected Listener listener;
	
	private boolean clickable = true;
	private Color backgroundColorClickable;
	private Color backgroundColorUnclickable;
	
	/**
	 * constructor
	 * @param x x position of upper left corner
	 * @param y y position of upper left corner
	 * @param width width of button
	 * @param height height of button
	 * @param backgroundColor color to fill the button with
	 * @param text text to display on the button
	 * @param message message to send when clicked
	 * @param listener listener to send message to
	 */
	public Button(int x, int y, int width, int height, Color backgroundColor, String text, String message, Listener listener) {
		super(x, y, width, height);
		this.backgroundColor = backgroundColor;
		this.text = text;
		this.message = message;
		this.listener = listener;
		
		this.backgroundColorClickable = backgroundColor;
		this.backgroundColorUnclickable = backgroundColor.darker().darker();
	}
	
	/**
	 * allows/disallows the object to receive clicks
	 * @param clickable whether the object can receive clicks
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
		if(clickable)
			this.backgroundColor = backgroundColorClickable;
		else
			this.backgroundColor = backgroundColorUnclickable;
	}
	
	@Override
	protected abstract Shape makeShape(int x, int y, int width, int height);
	
	@Override
	public abstract void drawSelf(Graphics2D g);
	
	@Override
	public void clicked(MouseEvent click) {
		if(clickable) {
			super.clicked(click);
			listener.hear(message, this);
		}
	}
	
}
