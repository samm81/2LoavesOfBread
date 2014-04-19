package core.GUI;

import core.commodities.Commodity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.List;

import static java.awt.Color.BLACK;

/**
 * Contains all the bits and pieces for the make offer
 * popup. Also handles input for it.
 * 
 * @author Sam Maynard
 */
public class MakeOfferPopup extends GraphicalObject {
	
	List<Commodity> commodities;
	
	SelectInput select1;
	SelectInput select2;
	
	NumericInput input1;
	NumericInput input2;
	
	public MakeOfferPopup(int x, int y, int width, int height, DoubleBufferedCanvas canvas, java.util.List<Commodity> commodities) {
		super(x, y, width, height, canvas);
		
		this.commodities = commodities;
		
		int inputX = x + 37;
		int inputY = y + 30;
		input1 = new NumericInput(inputX, inputY, 37, 32, 2, canvas);
		input1.setFocused(true);
		inputX += 55 + 200 + 50 + 70;
		input2 = new NumericInput(inputX, inputY, 37, 32, 2, canvas);
		
		int selectX = x + 85;
		int selectY = y + 30;
		LinkedList<String> options = new LinkedList<String>();
		LinkedList<Color> colors = new LinkedList<Color>();
		for(Commodity commodity : commodities){
			options.add(commodity.name());
			colors.add(commodity.getColor());
		}
		select1 = new SelectInput(selectX, selectY, 215, 32, canvas, options, colors);
		selectX += 200 + 50 + 70 + 55;
		select2 = new SelectInput(selectX, selectY, 215, 32, canvas, options, colors);
	}
	
	public Integer getVolume1() {
		return input1.getInput();
	}
	
	public Integer getVolume2() {
		return input2.getInput();
	}
	
	public Commodity getCommodity1() {
		return this.commodities.get(select1.getSelection());
	}
	
	public Commodity getCommodity2() {
		return this.commodities.get(select2.getSelection());
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);

		int textX = this.x + 30 + 55 + 200 + 50;
		int textY = this.y + 60;
		g.setColor(BLACK);
		g.setFont(new Font("Sans Serif", Font.BOLD, 32));
		g.drawString("for", textX, textY);
		
		input1.drawSelf(g);
		input2.drawSelf(g);
		
		select1.drawSelf(g);
		select2.drawSelf(g);
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
		
		if(select1.pointInBounds(x, y))
			select1.clicked(click);
		
		if(select2.pointInBounds(x, y))
			select2.clicked(click);
	}
	
	@Override
	public void keyPressed(KeyEvent keyPress) {
		if(keyPress.getKeyCode() == KeyEvent.VK_TAB) {
			if(!input1.focused()){
				input1.setFocused(true);
				input2.setFocused(false);
			}else{
				input1.setFocused(false);
				input2.setFocused(true);
			}
		}
		
		if(keyPress.getKeyCode() == KeyEvent.VK_ENTER) {
			canvas.message("OfferMade");
		}
		input1.keyPressed(keyPress);
		input2.keyPressed(keyPress);
	}
	
}
