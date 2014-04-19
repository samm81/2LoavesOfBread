package core.GUI;

import core.actors.Player;
import core.commodities.Commodity;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import static java.awt.Color.BLACK;

/**
 * Class to hold the graphical representation of the inventory:
 * player's amount of stuff, the examine trades button and the
 * make trade button
 *
 * @author Sam Maynard
 */
public class Inventory extends GraphicalObject {

    List<Commodity> commodities;
    Player player;

    public Inventory(int x, int y, int width, int height, DoubleBufferedCanvas canvas, java.util.List<Commodity> commodities, Player player) {
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

        int titleX = x + 30;
        int titleY = y + 30;
        g.setFont(new Font("Sans Serif", Font.BOLD, 22));
        g.setColor(BLACK);
        g.drawString("Inventory:", titleX, titleY);

        int commodityX = titleX + 10;
        int commodityY = titleY + 25;
        for (Commodity commodity : this.commodities) {
            String name = commodity.name();
            Integer volume = player.getVolumes().get(commodity);
            g.setFont(new Font("Sans Serif", Font.BOLD, 16));
            g.setColor(BLACK);
            g.drawString(volume + " " + name, commodityX, commodityY);

            commodityY += 25;
            if (commodityY > (this.y + this.height - 20)) {
                commodityY = titleY + 25;
                commodityX += 100;
            }
        }
    }

}
