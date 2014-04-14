package core.GUI;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;

import core.commodities.Commodity;

/**
 * Contains all the bits and pieces for the make offer
 * popup. Also handles input for it.
 * 
 * @author Sam Maynard
 */
public class MakeOfferPopup extends GraphicalObject {
	
	LinkedList<Commodity> commodities;
	int commodity1Index = 0;
	int commodity2Index = 0;
	
	SelectionButton leftUp;
	SelectionButton leftDown;
	SelectionButton rightUp;
	SelectionButton rightDown;
	
	TextInput input1;
	TextInput input2;
	
	public MakeOfferPopup(int x, int y, int width, int height, DoubleBufferedCanvas canvas, LinkedList<Commodity> commodities) {
		super(x, y, width, height, canvas);
		
		this.commodities = commodities;
		
		int inputX = x + 37;
		int inputY = y + 30;
		input1 = new TextInput(inputX, inputY, 37, 32, 2, canvas);
		input1.setFocused(true);
		inputX += 55 + 200 + 50 + 70;
		input2 = new TextInput(inputX, inputY, 37, 32, 2, canvas);
		
		int buttonX = x + 30 + 55 + 200;
		int buttonY = y + 60;
		this.leftUp = new SelectionButton(buttonX, buttonY - 27, 15, 15, true);
		this.leftDown = new SelectionButton(buttonX, buttonY - 7, 15, 15, false);
		buttonX += 50 + 70 + 55 + 200;
		this.rightUp = new SelectionButton(buttonX, buttonY - 27, 15, 15, true);
		this.rightDown = new SelectionButton(buttonX, buttonY - 7, 15, 15, false);
	}
	
	public int getVolume1() {
		//TODO
		return 0;
	}
	
	public int getVolume2() {
		//TODO
		return 0;
	}
	
	public Commodity getCommodity1() {
		return this.commodities.get(this.commodity1Index);
	}
	
	public Commodity getCommodity2() {
		return this.commodities.get(this.commodity2Index);
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	private boolean commodity1IndexBottomed() {
		return this.commodity1Index == 0;
	}
	
	private boolean commodity1IndexTopped() {
		return this.commodity1Index == this.commodities.size() - 1;
	}
	
	private boolean commodity2IndexBottomed() {
		return this.commodity2Index == 0;
	}
	
	private boolean commodity2IndexTopped() {
		return this.commodity2Index == this.commodities.size() - 1;
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);
		
		Commodity commodity1 = this.commodities.get(this.commodity1Index);
		Commodity commodity2 = this.commodities.get(this.commodity2Index);
		String commodity1Name = commodity1.name();
		String commodity2Name = commodity2.name();
		Color commodity1Color = commodity1.getColor();
		Color commodity2Color = commodity2.getColor();
		
		int textX = this.x + 30;
		input1.drawSelf(g);
		
		int textY = this.y + 60;
		
		g.setFont(new Font("Sans Serif", Font.BOLD, 32));
		
		textX += 55;
		g.setColor(commodity1Color);
		g.drawString(commodity1Name, textX, textY);
		
		textX += 200;
		if(!commodity1IndexBottomed())
			this.leftUp.drawSelf(g);
		if(!commodity1IndexTopped())
			this.leftDown.drawSelf(g);
		
		textX += 50;
		g.setColor(BLACK);
		g.drawString("for", textX, textY);
		
		textX += 70;
		input2.drawSelf(g);
		
		textX += 55;
		g.setColor(commodity2Color);
		g.drawString(commodity2Name, textX, textY);
		
		textX += 200;
		if(!commodity2IndexBottomed())
			this.rightUp.drawSelf(g);
		if(!commodity2IndexTopped())
			this.rightDown.drawSelf(g);
	}
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		int x = click.getX();
		int y = click.getY();
		
		if(input1.pointInBounds(x, y)) {
			input1.clicked(click);
		} else {
			input1.setFocused(false);
		}
		
		if(input2.pointInBounds(x, y)) {
			input2.clicked(click);
		} else {
			input2.setFocused(false);
		}
		
		if(this.leftUp.contains(x, y)) {
			if(!commodity1IndexBottomed())
				this.commodity1Index--;
		} else if(this.leftDown.contains(x, y)) {
			if(!commodity1IndexTopped())
				this.commodity1Index++;
		} else if(this.rightUp.contains(x, y)) {
			if(!commodity2IndexBottomed())
				this.commodity2Index--;
		} else if(this.rightDown.contains(x, y)) {
			if(!commodity2IndexTopped())
				this.commodity2Index++;
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent keyPress) {
		if(keyPress.getKeyCode() == KeyEvent.VK_TAB) {
			//TODO: transfer focus
		}
		
		if(keyPress.getKeyCode() == KeyEvent.VK_ENTER) {
			//TODO: go
		}
		input1.keyPressed(keyPress);
		input2.keyPressed(keyPress);
		
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
