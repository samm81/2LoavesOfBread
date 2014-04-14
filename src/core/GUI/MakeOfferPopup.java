package core.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;

import core.commodities.Commodity;

/**
 * Contains all the bits and pieces for the make offer
 * popup. Also handles input for it.
 * 
 * @author Sam Maynard
 *
 */
public class MakeOfferPopup extends GraphicalObject {

	LinkedList<Commodity> commodities;
	int commodity1Index = 0;
	int commodity2Index = 0;

	SelectionButton leftUp;
	SelectionButton leftDown;
	SelectionButton rightUp;
	SelectionButton rightDown;

	char[] volumes = { ' ', ' ', ' ', ' ' };
	int editingChar = 0;

	long time;
	long blink = 450;
	boolean drawingCursor = true;

	public MakeOfferPopup(int x, int y, int width, int height, DoubleBufferedCanvas canvas, LinkedList<Commodity> commodities) {
		super(x, y, width, height, canvas);

		this.commodities = commodities;

		int buttonx = x + 30 + 55 + 200;
		int buttony = y + 60;
		leftUp = new SelectionButton(buttonx, buttony - 27, 15, 15, true);
		leftDown = new SelectionButton(buttonx, buttony - 7, 15, 15, false);
		buttonx += 50 + 70 + 55 + 200;
		rightUp = new SelectionButton(buttonx, buttony - 27, 15, 15, true);
		rightDown = new SelectionButton(buttonx, buttony - 7, 15, 15, false);
	}

	public int getVolume1() {
		String volume1String = String.valueOf(volumes[0]) + String.valueOf(volumes[1]);
		return Integer.parseInt(volume1String);
	}

	public int getVolume2() {
		String volume2String = String.valueOf(volumes[2]) + String.valueOf(volumes[3]);
		return Integer.parseInt(volume2String);
	}

	public Commodity getCommodity1() {
		return commodities.get(commodity1Index);
	}

	public Commodity getCommodity2() {
		return commodities.get(commodity2Index);
	}

	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new RoundRectangle2D.Float(x, y, width, height, 25, 25);
	}

	private boolean commodity1IndexBottomed() {
		return commodity1Index == 0;
	}

	private boolean commodity1IndexTopped() {
		return commodity1Index == commodities.size() - 1;
	}

	private boolean commodity2IndexBottomed() {
		return commodity2Index == 0;
	}

	private boolean commodity2IndexTopped() {
		return commodity2Index == commodities.size() - 1;
	}

	@Override
	public void drawSelf(Graphics2D g) {
		this.drawOutline(g);

		Commodity commodity1 = commodities.get(commodity1Index);
		Commodity commodity2 = commodities.get(commodity2Index);
		String commodity1Name = commodity1.name();
		String commodity2Name = commodity2.name();
		Color commodity1Color = commodity1.getColor();
		Color commodity2Color = commodity2.getColor();

		String volume1 = String.valueOf(volumes[0]) + String.valueOf(volumes[1]);
		String volume2 = String.valueOf(volumes[2]) + String.valueOf(volumes[3]);

		int textx = x + 30;
		int texty = y + 60;

		g.setFont(new Font("Sans Serif", Font.BOLD, 32));

		g.setColor(Color.BLACK);
		g.drawString("__", textx, texty);
		g.drawString(volume1, textx, texty);

		textx += 55;
		g.setColor(commodity1Color);
		g.drawString(commodity1Name, textx, texty);

		textx += 200;
		if(!commodity1IndexBottomed())
			leftUp.drawSelf(g);
		if(!commodity1IndexTopped())
			leftDown.drawSelf(g);

		textx += 50;
		g.setColor(Color.BLACK);
		g.drawString("for", textx, texty);

		textx += 70;
		g.setColor(Color.BLACK);
		g.drawString("__", textx, texty);
		g.drawString(volume2 + "", textx, texty);

		textx += 55;
		g.setColor(commodity2Color);
		g.drawString(commodity2Name, textx, texty);

		textx += 200;
		if(!commodity2IndexBottomed())
			rightUp.drawSelf(g);
		if(!commodity2IndexTopped())
			rightDown.drawSelf(g);

		//cursor
		long now = System.currentTimeMillis();
		long diff = now - time;
		if(diff > blink) {
			drawingCursor = !drawingCursor;
			time = now;
		}
		if(drawingCursor) {
			g.setColor(Color.BLACK);
			int cursorx = 0;
			switch(editingChar) {
			case 0:
				cursorx = x + 30;
				break;
			case 1:
				cursorx = x + 48;
				break;
			case 2:
				cursorx = x + 405;
				break;
			case 3:
				cursorx = x + 424;
				break;
			case 4:
				cursorx = x + 442;
				break;
			}
			g.fillRect(cursorx, texty - 30, 3, 30);
		}
	}

	@Override
	public void clicked(MouseEvent e) {
		super.clicked(e);
		int x = e.getX();
		int y = e.getY();

		if(leftUp.contains(x, y)) {
			if(!commodity1IndexBottomed())
				commodity1Index--;
		} else if(leftDown.contains(x, y)) {
			if(!commodity1IndexTopped())
				commodity1Index++;
		} else if(rightUp.contains(x, y)) {
			if(!commodity2IndexBottomed())
				commodity2Index--;
		} else if(rightDown.contains(x, y)) {
			if(!commodity2IndexTopped())
				commodity2Index++;
		}
	}

	@Override
	public void keyPressed(KeyEvent keyPress) {
		if(keyPress.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			int indexToClear = editingChar - 1;
			if(indexToClear >= 0) {
				volumes[indexToClear] = ' ';
				editingChar--;
			}
		}

		char key = keyPress.getKeyChar();
		try {
			Integer.parseInt(String.valueOf(key));
		} catch(NumberFormatException e) {
			return;
		}

		if(editingChar < 4) {
			volumes[editingChar] = key;
			editingChar++;
		}
	}

	/**
	 * Class that holds the graphical representation of the
	 * up/down arrows that select the commodity.
	 * 
	 * @author Sam Maynard
	 *
	 */
	private class SelectionButton {

		private Polygon triangle;

		public boolean contains(int x, int y) {
			return triangle.contains(x, y);
		}

		public SelectionButton(int x, int y, int width, int height, boolean up) {
			triangle = new Polygon();
			if(up) {
				triangle.addPoint(x, y + height);
				triangle.addPoint(x + width / 2, y);
				triangle.addPoint(x + width, y + height);
			} else {
				triangle.addPoint(x, y);
				triangle.addPoint(x + width / 2, y + height);
				triangle.addPoint(x + width, y);
			}
		}

		public void drawSelf(Graphics2D g) {
			g.setColor(Color.RED);
			g.fillPolygon(triangle);
		}

	}

}
