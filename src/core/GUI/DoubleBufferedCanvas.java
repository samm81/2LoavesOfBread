package core.GUI;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Abstract class to deal with making a Double Buffered Canvas.
 * @author Sam Maynard
 * 
 */
@SuppressWarnings("serial")
abstract class DoubleBufferedCanvas extends Canvas implements Runnable {
	
	protected Thread thread;
	
	protected int fps;
	private int pauseTime;
	private long lastTime;
	
	protected Image buffer;
	protected int bufferWidth;
	protected int bufferHeight;
	protected Graphics bufferGraphics;
	
	protected HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	protected LinkedList<KeyEvent> keyPresses = new LinkedList<KeyEvent>();
	protected LinkedList<MouseEvent> mouseClicks = new LinkedList<MouseEvent>();
	
	private FPSCounter fpsCounter;
	
	private boolean initialized = false;
	
	/**
	 * constructor
	 * @param fps the frames per second for which the canvas is to run at
	 */
	public DoubleBufferedCanvas(int fps) {
		this(fps, 6f);
	}
	
	/**
	 * constructor
	 * @param fps frames per second to run the canvas at
	 * @param fpsCounterUpdatesPerSecond number of times per second to update the FPS counter
	 */
	public DoubleBufferedCanvas(int fps, float fpsCounterUpdatesPerSecond) {
		super();
		
		this.fps = fps;
		if(fps == 0)
			this.pauseTime = 0;
		else
			this.pauseTime = (int) (1000f / (float) fps);
		
		fpsCounter = new FPSCounter(fpsCounterUpdatesPerSecond);
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				keys.put(key, true);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				keys.put(key, false);
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				keyPresses.add(e);
			}
			
		});
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseClicks.add(e);
			}
			
		});
		
		thread = new Thread(this);
	}
	
	/**
	 * Checks if the given key is pressed.
	 * @param keyEvent key to check
	 * @return true if the key is being pressed, false otherwise
	 */
	public boolean keyDown(int keyEvent) {
		return keys.get(keyEvent) != null && keys.get(keyEvent) == true;
	}
	
	/**
	 * Checks if there are any key presses unprocessed
	 * @return true if there are key presses waiting, false otherwise
	 */
	public boolean keyPressesWaiting() {
		return keyPresses.size() != 0;
	}
	
	/**
	 * returns the key press queue and clears it
	 * @return key press queue with all KeyEvents that have occured
	 */
	public LinkedList<KeyEvent> flushKeyPressQueue() {
		LinkedList<KeyEvent> keyPresses = new LinkedList<KeyEvent>();
		for(KeyEvent keyPress : this.keyPresses)
			keyPresses.add(keyPress);
		this.keyPresses.clear();
		return keyPresses;
	}
	
	/**
	 * Checks if there are mouse clicks unprocessed
	 * @return true if there are mouse clicks waiting, false otherwise
	 */
	public boolean mouseClicksWaiting() {
		return mouseClicks.size() != 0;
	}
	
	/**
	 * returns the mouse click queue and clears it
	 * @return mouse click queue with all MouseEvents that have occurred
	 */
	public LinkedList<MouseEvent> flushMouseClickQueue() {
		LinkedList<MouseEvent> mouseClicks = new LinkedList<MouseEvent>();
		for(MouseEvent mouseClick : this.mouseClicks)
			mouseClicks.add(mouseClick);
		this.mouseClicks.clear();
		return mouseClicks;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	/**
	 * allows variables to be initialized
	 */
	abstract void init();
	
	@Override
	public void paint(Graphics g) {
		if(!initialized) {
			init();
			initialized = true;
		}
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		if(buffer == null || bufferGraphics == null || width != bufferWidth || height != bufferHeight)
			resetBuffer();
		
		bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
		draw(bufferGraphics);
		fpsCounter.paintSelf(width - 40, 30, bufferGraphics);
		g.drawImage(buffer, 0, 0, null);
	}
	
	/**
	 * resets the buffer
	 */
	private void resetBuffer() {
		bufferWidth = this.getWidth();
		bufferHeight = this.getHeight();
		
		if(bufferGraphics != null) {
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if(buffer != null) {
			buffer.flush();
			buffer = null;
		}
		System.gc();
		
		buffer = createImage(bufferWidth, bufferHeight);
		bufferGraphics = buffer.getGraphics();
	}
	
	/**
	 * actually draws the image
	 * @param g graphics to draw with
	 */
	abstract void draw(Graphics g);
	
	@Override
	public void run() {
		while(Thread.currentThread() == thread) {
			if(initialized) {
				long time = System.currentTimeMillis();
				long diff = time - lastTime;
				
				if(diff > pauseTime){
					updateVars();
					repaint();
					
					lastTime = time;
				}
				processInputs();
			}
		}
	}
	
	/**
	 * starts the canvas animation
	 */
	public void start() {
		thread.start();
	}
	
	/**
	 * for any global variable updating that may need to be done
	 */
	abstract protected void updateVars();
	
	/**
	 * allows for the processing of mouse clicks and key presses
	 */
	abstract protected void processInputs();
	
	/**
	 * Class for creating an FPS Counter
	 * @author Sam Maynard
	 *
	 */
	private class FPSCounter {
		
		long lastTime;
		
		int f = 0;
		
		long lastUpdate;
		float updateTime;
		
		/**
		 * default constructor
		 */
		@SuppressWarnings("unused")
		public FPSCounter() {
			this(6f);
		}
		
		/**
		 * constructor to set the number of times the counter updates per second
		 * @param updatesPerSecond number of times the counter updates per second
		 */
		public FPSCounter(float updatesPerSecond) {
			this.updateTime = 1000f / updatesPerSecond;
			lastTime = System.currentTimeMillis();
			lastUpdate = lastTime;
		}
		
		/**
		 * paints the FPS counter at a given x and y with Graphics object g
		 * @param x x coordinate of the FPS counter
		 * @param y y coordinate of the FPS counter
		 * @param g Graphics object to draw the FPS counter with
		 */
		public void paintSelf(int x, int y, Graphics g) {
			tick();
			
			g.setFont(new Font("Courier New", Font.BOLD, 26));
			g.setColor(Color.RED);
			g.drawString("" + f, x, y);
		}
		
		/**
		 * used to track the frames per second
		 */
		private void tick() {
			long time = System.currentTimeMillis();
			
			long diff = time - lastUpdate;
			if(diff > updateTime) {
				diff = time - lastTime;
				f = (int) (1000f / diff);
				lastUpdate = time;
			}
			
			lastTime = time;
		}
		
	}
	
}
