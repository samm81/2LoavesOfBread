package core.GUI;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * Class that holds the graphical representation of
 * the go button on the make trade popup.
 *
 * @author Sam Maynard
 */
public class GoButton extends Button {

	public GoButton(int x, int y, int width, int height, Listener listener) {
		super(x, y, width, height, Color.GREEN, "GO", "OfferMade", listener);
	}

    @Override
    protected Shape makeShape(int x, int y, int width, int height) {
        return new RoundRectangle2D.Float(x, y, width, height, 15, 15);
    }

    @Override
    public void drawSelf(Graphics2D g) {
        this.drawOutline(backgroundColor, g);

        int textX = x + 7;
        int textY = y + 40;

        g.setFont(new Font("Sans Serif", Font.BOLD, 40));
        g.setColor(BLACK);
        g.drawString(message, textX, textY);
    }

}
