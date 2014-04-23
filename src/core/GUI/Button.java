package core.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;


public abstract class Button extends GraphicalObject {
	
	protected Color backgroundColor;
	protected String text;
	protected String message;
	protected Listener listener;

	private Color backgroundColorClickable;
	private Color backgroundColorUnclickable;
	
	public Button(int x, int y, int width, int height, Color backgroundColor, String text, String message, Listener listener) {
		super(x, y, width, height);
		this.backgroundColor = backgroundColor;
		this.text = text;
		this.message = message;
		this.listener = listener;
		
		this.backgroundColorClickable = backgroundColor;
		this.backgroundColorUnclickable = backgroundColor.darker().darker();
	}
	
	
	public void setClickable(boolean clickable) {
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
		super.clicked(click);
		listener.hear(message);
	}
	
}
