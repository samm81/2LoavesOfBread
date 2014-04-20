package core.GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import static java.awt.Color.BLACK;


public class Button extends GraphicalObject {
	
	Color backgroundColor;
	String text;
	String message;
	
	public Button(int x, int y, int width, int height, Color backgroundColor, String text, String message, DoubleBufferedCanvas canvas) {
		super(x, y, width, height, canvas);
		this.backgroundColor = backgroundColor;
		this.text = text;
		this.message = message;
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 20, 20);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(backgroundColor, g);

		int fontHeight = this.height / 2;
		
        g.setFont(new Font("Sans Serif", Font.BOLD, fontHeight));
        FontMetrics metrics = g.getFontMetrics();
        
        int textWidth = metrics.stringWidth(text);
        int textXPadding = (this.width - textWidth) / 2;
        
        int textHeight = fontHeight;
        int textYPadding = (this.height - textHeight) / 2;
        textYPadding += 3; // by experimentation
        
        int textX = this.x + textXPadding;
        int textY = this.y + this.height - textYPadding;

        g.setColor(BLACK);
        g.drawString(text, textX, textY);
	}
	
	@Override
	public void clicked(MouseEvent e) {
		super.clicked(e);
		this.canvas.message(message);
	}
	
}
