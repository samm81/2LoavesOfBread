package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Class that holds the graphical representation of
 * the go button on the make trade popup.
 * 
 * @author Sam Maynard
 *
 */
public class GoButton extends GraphicalObject {

	public GoButton(int x, int y, int width, int height, DoubleBufferedCanvas canvas) {
		super(x, y, width, height, canvas);
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 15, 15);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(Color.GREEN, g);

		int textx = x + 7;
		int texty = y + 40;

		g.setFont(new Font("Sans Serif", Font.BOLD, 40));
		g.setColor(Color.BLACK);
		g.drawString("GO", textx, texty);
	}

	@Override
	public void clicked(MouseEvent e) {
		super.clicked(e);
		canvas.message("OfferMade");
	}

}
