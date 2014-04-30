package core.GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;


public class TransparencyOverlay extends GraphicalObject {

    Color color;
    Listener listener;

    public TransparencyOverlay(int width, int height, Color color, Listener listener) {
        super(0, 0, width, height);
        this.color = color;
        this.listener = listener;
    }

    @Override
    protected Shape makeShape(int x, int y, int width, int height) {
        return new Rectangle2D.Float(x, y, width, height);
    }

    @Override
    public void drawSelf(Graphics2D g) {
        g.setColor(this.color);
        g.fill(this.shape);
    }

    @Override
    public void clicked(MouseEvent e) {
        super.clicked(e);
        listener.hear("ClearOverlay", this);
    }

}
