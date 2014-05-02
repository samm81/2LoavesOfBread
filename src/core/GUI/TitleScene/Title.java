package core.GUI.TitleScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import core.GUI.GraphicalObject;
import core.GUI.Listener;


public class Title extends GraphicalObject {

	Listener listener;
	
	public Title(int x, int y, int width, int height, Listener listener) {
		super(x, y, width, height);
		this.listener = listener;
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle2D.Float(x, y, width, height);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(Color.WHITE, 0, g);
		
		g.setFont(new Font("Serif", Font.BOLD, 56));
		g.setColor(Color.BLACK);
		g.drawString("Two Loaves of Bread", 100, 100);
		
		g.setFont(new Font("Serif", Font.PLAIN, 28));
		g.setColor(Color.GREEN);
		g.drawString("PLAY", 150, 200);
		
		g.setColor(Color.BLUE);
		g.drawString("TUTORIAL", 150, 250);
	}
	
}
