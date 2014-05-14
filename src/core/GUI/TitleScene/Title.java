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
		
		g.setFont(new Font("Serif", Font.BOLD, 28));
		g.setColor(Color.GREEN);
		g.drawString("PLAY", 100, 280);
		
		//g.setColor(Color.BLUE);
		//g.drawString("TUTORIAL", 150, 250);
		
		String str1 = "You are a young boy circa 1000 BCE. Your mother is sick and dying, and the";
		String str2 = "village doctor will only cure her in exchange for 40 watermelons. Otherwise,";
		String str3 = "your mother will die within 8 minutes. You have on your person only two loaves";
		String str4 = "of bread. You enter the busy market, where hundreds of traders are exchanging";
		String str5 = "goods at a rapid pace. You begin, determined to save your mother.";
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		g.drawString(str1, 100, 150);
		g.drawString(str2, 100, 170);
		g.drawString(str3, 100, 190);
		g.drawString(str4, 100, 210);
		g.drawString(str5, 100, 230);
	}
	
}
