package core.GUI.MakeOfferScene;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import core.GUI.GraphicalObject;
import static java.awt.Color.RED;


public class SelectInput extends GraphicalObject {
	
	LinkedList<String> options;
	LinkedList<Color> colors;
	int selection;
	
	SelectionButton up;
	SelectionButton down;
	
	public SelectInput(int x, int y, int width, int height, LinkedList<String> options, LinkedList<Color> colors) {
		super(x, y, width, height);
		this.options = options;
		this.colors = colors;
		this.selection = 0;
		
		int buttonX = x + 200;
		int buttonY = y + 30;
		this.up = new SelectionButton(buttonX, buttonY - 30, 18, 18, true);
		this.down= new SelectionButton(buttonX, buttonY - 7, 18, 18, false);
	}
	
	
	public int getSelection() {
		return this.selection;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	private boolean selectionIndexBottomed() {
		return this.selection == 0;
	}
	
	private boolean selectionIndexTopped() {
		return this.selection == this.options.size() - 1;
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		String option = options.get(selection);
		Color color = colors.get(selection);
		
		g.setColor(color);
		g.setFont(new Font("Sans Serif", Font.BOLD, 32));
		
		int textx = this.x;
		int texty = this.y + this.height;
		
		g.drawString(option, textx, texty);
		
		if(!selectionIndexBottomed())
			this.up.drawSelf(g);
		if(!selectionIndexTopped())
			this.down.drawSelf(g);
	}
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		
		int x = click.getX();
		int y = click.getY();
		
		if(this.up.contains(x, y)) {
			if(!selectionIndexBottomed())
				this.selection--;
		} else if(this.down.contains(x, y)) {
			if(!selectionIndexTopped())
				this.selection++;
		}
	}
	
	/**
	 * Class that holds the graphical representation of the
	 * up/down arrows that select the commodity.
	 * 
	 * @author Sam Maynard
	 */
	private class SelectionButton {
		
		private Polygon triangle;
		
		public SelectionButton(int x, int y, int width, int height, boolean up) {
			this.triangle = new Polygon();
			if(up) {
				this.triangle.addPoint(x, y + height);
				this.triangle.addPoint(x + width / 2, y);
				this.triangle.addPoint(x + width, y + height);
			} else {
				this.triangle.addPoint(x, y);
				this.triangle.addPoint(x + width / 2, y + height);
				this.triangle.addPoint(x + width, y);
			}
		}
		
		public boolean contains(int x, int y) {
			return this.triangle.contains(x, y);
		}
		
		public void drawSelf(Graphics2D g) {
			g.setColor(RED);
			g.fillPolygon(this.triangle);
		}
		
	}
	
}
