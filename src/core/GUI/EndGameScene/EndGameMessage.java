package core.GUI.EndGameScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import core.GUI.GraphicalObject;


public class EndGameMessage extends GraphicalObject {
	
	public EndGameMessage(int x, int y) {
		super(x, y, 200, 90);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.BOLD, 48));
		g.drawString("GAME OVER", x, y + height - 40);
		g.setFont(new Font("Serif", Font.PLAIN, 32));
		g.drawString("You have run out of time", x - 5, y + height);
	}
	
}
