package core.GUI;

import static java.awt.Color.BLACK;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import core.Offer;
import core.actors.Player;
import core.commodities.Commodity;

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

    public Inventory(int x, int y, int width, int height, List<Commodity> commodities, Player player) {
        super(x, y, width, height);
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
            g.setColor(commodity.getColor());
            g.drawString(volume + " " + name, commodityX, commodityY);

            commodityY += 25;
            if (commodityY > (this.y + this.height - 20)) {
                commodityY = titleY + 25;
                commodityX += 100;
            }
        }
        
        int offerX = x + 480;
        int offerY = y + 55;
        
        Offer offer = player.getBestOffer();
        if(offer == null){
        	g.setColor(Color.RED);
        	g.drawString("no pending offer", offerX, offerY);
        }else{
        	g.setColor(Color.GREEN);
        	offerX -= 40;
        	offerY -= 20;
        	g.drawString("      pending offer", offerX, offerY);
        	offerY += 20;
        	g.setColor(offer.getCommodity1().getColor());
        	g.drawString("trade at most " + offer.getMaxTradeVolume() + " " + offer.getCommodity1(), offerX, offerY);
        	offerY += 20;
        	g.setColor(offer.getCommodity2().getColor());
        	g.drawString("get at least  " + offer.getMinReceive() + " " + offer.getCommodity2(), offerX, offerY);
        }
        
    }

}
