package core.GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.awt.Color.BLACK;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;

/**
 * Object that allows the user to enter numeric input
 * @author Sam Maynard
 *
 */
public class NumericInput extends GraphicalObject {
	
	private String underscores = "";
	
	private int editingChar = 0;
	private char[] input;
	
	private boolean focused = false;
	
	private long time = 0;
	private long blink = 450;
	private boolean drawingCursor = true;
	
	/**
	 * constructor
	 * @param inputChars number of characters to allowed to be inputted
	 */
	public NumericInput(int x, int y, int width, int height, int inputChars) {
		super(x, y, width, height);
		input = new char[inputChars];
		for(int i = 0; i < inputChars; i++) {
			underscores += "_";
			input[i] = ' ';
		}
	}
	
	public boolean focused() {
		return this.focused;
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	/**
	 * tries to get the current user input as an int
	 * @return the user's input as an int
	 */
	public Integer getInput() {
		Integer num = null;
		try {
			num = Integer.parseInt(String.valueOf(input[0]));
		} catch(NumberFormatException e) {}
		try {
			num = Integer.parseInt(String.valueOf(input[1]));
		} catch(NumberFormatException e) {}
		try {
			num = Integer.parseInt(String.valueOf(input));
		} catch(NumberFormatException e) {}
		return num;
	}
	
	@Override
	protected Shape makeShape(int x, int y, int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public void drawSelf(Graphics2D g) {
		int fontHeight = this.height;
		
		g.setFont(new Font("Sans Serif", Font.BOLD, fontHeight));
		
		int textX = this.x;
		int textY = this.y + this.height;
		
		g.setColor(BLACK);
		g.drawString(underscores, textX, textY);
		
		String text = String.valueOf(input);
		g.drawString(text, textX, textY);
		
		long now = System.currentTimeMillis();
		long diff = now - this.time;
		if(diff > this.blink) {
			this.drawingCursor = !this.drawingCursor;
			this.time = now;
		}
		if(this.focused && this.drawingCursor) {
			int cursorX = this.x + 3;
			cursorX += editingChar * 15;
			int cursorY = textY - 30;
			g.fillRect(cursorX, cursorY, 3, 30);
		}
	}
	
	@Override
	public void clicked(MouseEvent e) {
		super.clicked(e);
		this.focused = true;
	}
	
	@Override
	public void keyPressed(KeyEvent keyPress) {
		super.keyPressed(keyPress);
		
		if(focused) {
			if(keyPress.getKeyCode() == VK_BACK_SPACE && editingChar - 1 >= 0) {
				this.editingChar--;
				this.input[editingChar] = ' ';
			} else if(editingChar + 1 <= input.length) {
				char character = keyPress.getKeyChar();
				int num = -1;
				try {
					num = Integer.parseInt(String.valueOf(character));
				} catch(NumberFormatException e) {}
				if(num != -1) {
					this.input[editingChar] = character;
					editingChar++;
				}
			}
		}
	}
	
}
