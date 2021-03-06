package core;

/**
 * Abstract class to encapsulate any thread that has a delta timeLimit.
 *
 * @author Sam Maynard
 */
public abstract class TickableThread implements Runnable {
	
	double dt;
	
	private boolean initialized = false;
	private int pauseTime;
	private long lastTime;
	protected long time;
	
	Thread thread;
	
	/**
	 * constructor
	 * @param dt how often to tick
	 */
	public TickableThread(double dt) {
		super();
		this.dt = dt;
		
		pauseTime = (int) (1000f / (float) dt);
		
		this.thread = new Thread(this);
	}
	
	/**
	 * starts the thread
	 */
	public void start() {
		initialize();
		time = System.currentTimeMillis();
		lastTime = time;
		initialized = true;
		thread.start();
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			if(initialized) {
				time = System.currentTimeMillis();
				long diff = time - lastTime;
				
				if(diff > pauseTime) {
					tick();
					lastTime = time;
				}
				Thread.yield();
			}
		}
	}
	
	/**
	 * allows for initialization of variables before the first tick
	 */
	protected abstract void initialize();
	
	/**
	 * runs every dt
	 */
	protected abstract void tick();
	
}
