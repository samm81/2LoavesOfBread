package core.GUI.ViewMarketScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;

import core.GUI.Button;
import core.GUI.Listener;

/**
 * Checkbox to allow the user to filter offers
 * @author Sam Maynard
 *
 */
public class MakingNonReduntantCheckbox extends Button {

	private boolean checked = true;
	private int checkedSquareOffest = 6;
	
	/**
	 * constructor
	 */
	public MakingNonReduntantCheckbox(int x, int y, int size, Listener listener) {
		super(x, y, size, size, Color.WHITE, "don't show reduntant offers", "MakeNonRedundant", listener);
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		drawOutline(g);
		
		if(checked){
			g.setColor(Color.BLACK);
			g.fillRect(x + checkedSquareOffest, y + checkedSquareOffest, width - checkedSquareOffest*2, height - checkedSquareOffest*2);
		}
		
		g.setFont(new Font("Sans Serif", Font.PLAIN, 18));
		g.setColor(Color.BLACK);
		g.drawString(text, x - 220, y + 22);
		
	}
	
	@Override
	public void clicked(MouseEvent click) {
		super.clicked(click);
		checked = !checked;
	}
	
}
