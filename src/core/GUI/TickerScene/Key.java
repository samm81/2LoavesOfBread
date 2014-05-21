package core.GUI.TickerScene;

import core.GUI.GraphicalObject;
import core.commodities.Commodity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import static java.awt.Color.BLACK;

/**
 * Class that holds the graphical representation of the key.
 *
 * @author Sam Maynard
 */
public class Key extends GraphicalObject {

    List<Commodity> commodities;

    /**
     * constructor
     * @param commodities list of commodities to display on the key
     */
    public Key(int x, int y, int width, int height, List<Commodity> commodities) {
        super(x, y, width, height);
        this.commodities = commodities;
    }

    @Override
    protected Shape makeShape(int x, int y, int width, int height) {
        return new RoundRectangle2D.Float(x, y, width, height, 10, 10);
    }

    @Override
    public void drawSelf(Graphics2D g) {
        drawOutline(g);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 18));
        FontMetrics metrics = g.getFontMetrics();

        int labelX = x + 10;
        int labelY = y + 15;

        g.setColor(BLACK);
        g.drawString("KEY:", labelX, labelY + 11);

        labelX += 50;

        for (Commodity commodity : this.commodities) {
            Color color = commodity.getColor();
            g.setColor(color);
            g.fillOval(labelX, labelY, 10, 10);
            String name = commodity.name();
            g.drawString(name, labelX + 15, labelY + 11);

            labelX += metrics.stringWidth(name) + 27;
        }
    }

}
