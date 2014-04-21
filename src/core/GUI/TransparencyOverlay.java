package core.GUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;


public class TransparencyOverlay extends GraphicalObject {

    Color color;
    Listener listener;

    public TransparencyOverlay(DoubleBufferedCanvas canvas, Color color) {
        super(0, 0, canvas.getWidth(), canvas.getHeight());
        this.color = color;
        this.listener = canvas;
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
        listener.hear("ClearOverlays");
    }

}
