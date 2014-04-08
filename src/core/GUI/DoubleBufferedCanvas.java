package core.GUI;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Abstract class to deal with making a Double Buffered Canvas.
 *
 * @author Sam Maynard
 */
@SuppressWarnings ("serial")
abstract class DoubleBufferedCanvas extends Canvas implements Runnable {

    protected Thread thread;

    protected int fps;
    protected Image buffer;
    protected int bufferWidth;
    protected int bufferHeight;
    protected Graphics bufferGraphics;
    protected HashMap<Integer, Boolean> keys = new HashMap<>();
    protected LinkedList<KeyEvent> keyPresses = new LinkedList<>();
    protected LinkedList<MouseEvent> mouseClicks = new LinkedList<>();
    private int pauseTime;
    private long lastTime;
    private FPSCounter fpsCounter;

    private boolean initialized = false;

    /**
     * constructor
     *
     * @param fps the frames per second for which the canvas is to run at
     */
    public DoubleBufferedCanvas(int fps) {
        this(fps, 6f);
    }

    /**
     * constructor
     *
     * @param fps                        frames per second to run the canvas at
     * @param fpsCounterUpdatesPerSecond number of times per second to update the FPS counter
     */
    public DoubleBufferedCanvas(int fps, float fpsCounterUpdatesPerSecond) {
        super();

        this.fps = fps;
        if (fps == 0)
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

                keyPresses.add(e);
            }

        });

        // now with a custom tolerance system
        addMouseListener(new MouseAdapter() {
            int x;
            int y;
            MouseEvent e;
            int tolerance = 15;

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                this.e = e;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (Math.abs(this.x - x) < tolerance && Math.abs(this.y - y) < tolerance)
                    mouseClicks.add(this.e);
            }

        });

        thread = new Thread(this);
    }

    /**
     * Checks if the given key is pressed.
     *
     * @param keyEvent key to check
     * @return true if the key is being pressed, false otherwise
     */
    public boolean keyDown(int keyEvent) {
        return keys.get(keyEvent) != null && keys.get(keyEvent);
    }

    /**
     * Checks if there are any key presses unprocessed
     *
     * @return true if there are key presses waiting, false otherwise
     */
    public boolean keyPressesWaiting() {
        return keyPresses.size() != 0;
    }

    /**
     * returns the key press queue and clears it
     *
     * @return key press queue with all KeyEvents that have occured
     */
    public LinkedList<KeyEvent> flushKeyPressQueue() {
        LinkedList<KeyEvent> keyPresses = new LinkedList<>();
        for (KeyEvent keyPress : this.keyPresses)
            keyPresses.add(keyPress);
        this.keyPresses.clear();
        return keyPresses;
    }

    /**
     * Checks if there are mouse clicks unprocessed
     *
     * @return true if there are mouse clicks waiting, false otherwise
     */
    public boolean mouseClicksWaiting() {
        return mouseClicks.size() != 0;
    }

    /**
     * returns the mouse click queue and clears it
     *
     * @return mouse click queue with all MouseEvents that have occurred
     */
    public LinkedList<MouseEvent> flushMouseClickQueue() {
        LinkedList<MouseEvent> mouseClicks = new LinkedList<>();
        for (MouseEvent mouseClick : this.mouseClicks)
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
        if (!initialized) {
            init();
            initialized = true;
        }

        int width = this.getWidth();
        int height = this.getHeight();

        if (buffer == null || bufferGraphics == null || width != bufferWidth || height != bufferHeight)
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

        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (buffer != null) {
            buffer.flush();
            buffer = null;
        }
        System.gc();

        buffer = createImage(bufferWidth, bufferHeight);
        bufferGraphics = buffer.getGraphics();
    }

    /**
     * actually draws the image
     *
     * @param g graphics to draw with
     */
    abstract void draw(Graphics g);

    @Override
    public void run() {
        while (Thread.currentThread() == thread)
            if (initialized) {
                long time = System.currentTimeMillis();
                long diff = time - lastTime;

                if (diff > pauseTime) {
                    updateVars();
                    repaint();
                    lastTime = time;
                }
                System.out.println("About to Process");
                processInputs();
                Thread.yield();
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

    abstract public void message(String message);

    /**
     * Class for creating an FPS Counter
     *
     * @author Sam Maynard
     */
    private class FPSCounter {

        long lastTime;

        int f = 0;

        long lastUpdate;
        float updateTime;

        /**
         * default constructor
         */
        @SuppressWarnings ("unused")
        public FPSCounter() {
            this(6f);
        }

        /**
         * constructor to set the number of times the counter updates per second
         *
         * @param updatesPerSecond number of times the counter updates per second
         */
        public FPSCounter(float updatesPerSecond) {
            this.updateTime = 1000f / updatesPerSecond;
            lastTime = System.currentTimeMillis();
            lastUpdate = lastTime;
        }

        /**
         * paints the FPS counter at a given x and y with Graphics object g
         *
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
            if (diff > updateTime) {
                diff = time - lastTime;
                f = (int) (1000f / diff);
                lastUpdate = time;
            }

            lastTime = time;
        }

    }

}
