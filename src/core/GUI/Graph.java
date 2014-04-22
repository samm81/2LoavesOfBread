package core.GUI;

import core.commodities.Commodity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Class that holds the graphical representation of a commodity and
 * it's tickers.
 *
 * @author Sam Maynard
 */
public class Graph extends GraphicalObject {

    private Commodity commodity;

    public Graph(int x, int y, int width, int height, Commodity commodity) {
        super(x, y, width, height);
        this.commodity = commodity;
    }

    @Override
    protected Shape makeShape(int x, int y, int width, int height) {
        return new RoundRectangle2D.Float(x, y, width, height, 50, 50);
    }

    @Override
    public void drawSelf(Graphics2D g) {
        drawOutline(g);

        int titleX = x + 30;
        int titleY = y + 30;
        String name = commodity.name();
        g.setFont(new Font("Sans Serif", Font.BOLD, 22));
        g.setColor(commodity.getColor());
        g.drawString(name, titleX, titleY);

        int tickerX = x + 30;
        int tickerY = y + 50;
        int tickerWidth = width - 65;
        int tickerHeight = height - 70;

        int i = 0;
        for (Ticker ticker : commodity.getTickerCollection()) {
            ticker.drawSelf(tickerX + i*4, tickerY, tickerWidth, tickerHeight, g);
            ticker.drawLabel(tickerX + i * 25, tickerY - 1, tickerHeight, g);
            i++;
        }
    }

}
