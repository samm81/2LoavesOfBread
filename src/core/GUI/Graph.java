package core.GUI;

import core.commodities.Commodity;
import core.commodities.Ticker;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static java.awt.Color.*;

/**
 * Class that holds the graphical representation of a commodity and
 * it's tickers.
 *
 * @author Sam Maynard
 */
public class Graph extends GraphicalObject {

    private Commodity commodity;

    public Graph(int x, int y, int width, int height, DoubleBufferedCanvas canvas, Commodity commodity) {
        super(x, y, width, height, canvas);
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
        g.setColor(BLACK);
        g.drawString(name, titleX, titleY);

        int tickerX = x + 30;
        int tickerY = y + 50;
        int tickerWidth = width - 65;
        int tickerHeight = height - 70;

        g.setColor(new Color(.3f, .3f, .3f));
        double dy = tickerHeight / 7d;
        for (int i = 0; i <= 7; i++) {
            int x1 = tickerX;
            int y1 = tickerY + (int) (i * dy);
            int x2 = tickerX + tickerWidth;
            int y2 = y1;
            g.drawLine(x1, y1, x2, y2);
        }

        int i = 0;
        for (Ticker ticker : commodity.getTickerCollection()) {
            ticker.drawSelf(tickerX, tickerY, tickerWidth, tickerHeight, g);
            ticker.drawLabel(tickerX + i * 25, tickerY - 1, tickerHeight, 7, g);
            i++;
        }
    }

}
