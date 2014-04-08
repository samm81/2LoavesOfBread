package core.GUI;

import core.commodities.Commodity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;

import static java.awt.Color.*;
import static java.awt.event.KeyEvent.*;

/**
 * Contains all the bits and pieces for the make offer
 * popup. Also handles input for it.
 *
 * @author Sam Maynard
 */
public class MakeOfferPopup extends GraphicalObject {

    LinkedList<Commodity> commodities;
    int commodity1Index = 0;
    int commodity2Index = 0;

    SelectionButton leftUp;
    SelectionButton leftDown;
    SelectionButton rightUp;
    SelectionButton rightDown;

    char[] volumes = {' ', ' ', ' ', ' '};
    int editingChar = 0;

    long time;
    long blink = 450;
    boolean drawingCursor = true;

    public MakeOfferPopup(int x, int y, int width, int height, DoubleBufferedCanvas canvas, LinkedList<Commodity> commodities) {
        super(x, y, width, height, canvas);

        this.commodities = commodities;

        int buttonX = x + 30 + 55 + 200;
        int buttonY = y + 60;
        this.leftUp = new SelectionButton(buttonX, buttonY - 27, 15, 15, true);
        this.leftDown = new SelectionButton(buttonX, buttonY - 7, 15, 15, false);
        buttonX += 50 + 70 + 55 + 200;
        this.rightUp = new SelectionButton(buttonX, buttonY - 27, 15, 15, true);
        this.rightDown = new SelectionButton(buttonX, buttonY - 7, 15, 15, false);
    }

    public int getVolume1() {
        String volume1String = String.valueOf(this.volumes[0]) + String.valueOf(this.volumes[1]);
        return Integer.parseInt(volume1String);
    }

    public int getVolume2() {
        String volume2String = String.valueOf(this.volumes[2]) + String.valueOf(this.volumes[3]);
        return Integer.parseInt(volume2String);
    }

    public Commodity getCommodity1() {
        return this.commodities.get(this.commodity1Index);
    }

    public Commodity getCommodity2() {
        return this.commodities.get(this.commodity2Index);
    }

    @Override
    protected Shape makeShape(int x, int y, int width, int height) {
        return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
    }

    private boolean commodity1IndexBottomed() {
        return this.commodity1Index == 0;
    }

    private boolean commodity1IndexTopped() {
        return this.commodity1Index == this.commodities.size() - 1;
    }

    private boolean commodity2IndexBottomed() {
        return this.commodity2Index == 0;
    }

    private boolean commodity2IndexTopped() {
        return this.commodity2Index == this.commodities.size() - 1;
    }

    @Override
    public void drawSelf(Graphics2D g) {
        this.drawOutline(g);

        Commodity commodity1 = this.commodities.get(this.commodity1Index);
        Commodity commodity2 = this.commodities.get(this.commodity2Index);
        String commodity1Name = commodity1.name();
        String commodity2Name = commodity2.name();
        Color commodity1Color = commodity1.getColor();
        Color commodity2Color = commodity2.getColor();

        String volume1 = String.valueOf(this.volumes[0]) + String.valueOf(this.volumes[1]);
        String volume2 = String.valueOf(this.volumes[2]) + String.valueOf(this.volumes[3]);

        int textX = this.x + 30;
        int textY = this.y + 60;

        g.setFont(new Font("Sans Serif", Font.BOLD, 32));

        g.setColor(BLACK);
        g.drawString("__", textX, textY);
        g.drawString(volume1, textX, textY);

        textX += 55;
        g.setColor(commodity1Color);
        g.drawString(commodity1Name, textX, textY);

        textX += 200;
        if (!commodity1IndexBottomed())
            this.leftUp.drawSelf(g);
        if (!commodity1IndexTopped())
            this.leftDown.drawSelf(g);

        textX += 50;
        g.setColor(BLACK);
        g.drawString("for", textX, textY);

        textX += 70;
        g.setColor(BLACK);
        g.drawString("__", textX, textY);
        g.drawString(volume2 + "", textX, textY);

        textX += 55;
        g.setColor(commodity2Color);
        g.drawString(commodity2Name, textX, textY);
        //TODO: Incremented but never used?
        textX += 200;
        if (!commodity2IndexBottomed())
            this.rightUp.drawSelf(g);
        if (!commodity2IndexTopped())
            this.rightDown.drawSelf(g);

        //cursor
        long now = System.currentTimeMillis();
        long diff = now - this.time;
        if (diff > this.blink) {
            this.drawingCursor = !this.drawingCursor;
            this.time = now;
        }
        if (this.drawingCursor) {
            g.setColor(BLACK);
            int cursorX = 0;
            switch (this.editingChar) {
                case 0:
                    cursorX = this.x + 30;
                    break;
                case 1:
                    cursorX = this.x + 48;
                    break;
                case 2:
                    cursorX = this.x + 405;
                    break;
                case 3:
                    cursorX = this.x + 424;
                    break;
                case 4:
                    cursorX = this.x + 442;
                    break;
            }
            g.fillRect(cursorX, textY - 30, 3, 30);
        }
    }

    @Override
    public void clicked(MouseEvent e) {
        super.clicked(e);
        int x = e.getX();
        int y = e.getY();

        if (this.leftUp.contains(x, y)) {
            if (!commodity1IndexBottomed())
                this.commodity1Index--;
        } else if (this.leftDown.contains(x, y)) {
            if (!commodity1IndexTopped())
                this.commodity1Index++;
        } else if (this.rightUp.contains(x, y)) {
            if (!commodity2IndexBottomed())
                this.commodity2Index--;
        } else if (this.rightDown.contains(x, y)) {
            if (!commodity2IndexTopped())
                this.commodity2Index++;
        }
    }

    @Override
    public void keyPressed(KeyEvent keyPress) {
        if (keyPress.getKeyCode() == VK_BACK_SPACE) {
            int indexToClear = this.editingChar - 1;
            if (indexToClear >= 0) {
                this.volumes[indexToClear] = ' ';
                this.editingChar--;
            }
        }

        char key = keyPress.getKeyChar();
        try {
            Integer.parseInt(String.valueOf(key));
        } catch (NumberFormatException e) {
            return;
        }

        if (this.editingChar < 4) {
            this.volumes[editingChar] = key;
            this.editingChar++;
        }
    }

    /**
     * Class that holds the graphical representation of the
     * up/down arrows that select the commodity.
     *
     * @author Sam Maynard
     */
    private class SelectionButton {

        private Polygon triangle;

        public SelectionButton(int x, int y, int width, int height, boolean up) {
            this.triangle = new Polygon();
            if (up) {
                this.triangle.addPoint(x, y + height);
                this.triangle.addPoint(x + width / 2, y);
                this.triangle.addPoint(x + width, y + height);
            } else {
                this.triangle.addPoint(x, y);
                this.triangle.addPoint(x + width / 2, y + height);
                this.triangle.addPoint(x + width, y);
            }
        }

        public boolean contains(int x, int y) {
            return this.triangle.contains(x, y);
        }

        public void drawSelf(Graphics2D g) {
            g.setColor(RED);
            g.fillPolygon(this.triangle);
        }

    }

}
