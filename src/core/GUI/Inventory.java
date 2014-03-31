package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;

import core.Player;
import core.commodities.Commodity;

/**
 * Class to hold the graphical representation of the inventory:
 * player's amount of stuff, the examine trades button and the
 * make trade button
 * 
 * @author Sam Maynard
 *
 */
public class Inventory extends GraphicalObject {
	
	LinkedList<Commodity> commodities;
	Player player;
	
	public Inventory(int x, int y, int width, int height, DoubleBufferedCanvas canvas, LinkedList<Commodity> commodities, Player player) {
		super(x, y, width, height, canvas);
		this.commodities = commodities;
		this.player = player;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 15, 15);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(g);
		
		int titlex = x + 30;
		int titley = y + 30;
		g.setFont(new Font("Sans Serif", Font.BOLD, 22));
		g.setColor(Color.BLACK);
		g.drawString("Inventory:", titlex, titley);
		
		int commodityx = titlex + 10;
		int commodityy = titley + 25;
		for(Commodity commodity : commodities){
			String name = commodity.name();
			g.setFont(new Font("Sans Serif", Font.BOLD, 16));
			g.setColor(Color.BLACK);
			g.drawString("10 " + name, commodityx, commodityy );
			
			commodityy += 25;
			if(commodityy > (this.y + this.height - 20)){
				commodityy = titley + 25;
				commodityx += 100;
			}
		}
	}
	
}
