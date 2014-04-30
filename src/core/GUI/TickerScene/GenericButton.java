package core.GUI.TickerScene;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import core.GUI.Button;
import core.GUI.Listener;

public class GenericButton extends Button {
	
	public GenericButton(int x, int y, int width, int height, Color backgroundColor, String text, String message, Listener listener) {
		super(x, y, width, height, backgroundColor, text, message, listener);
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
	
}
