package core;
public abstract class Simulation implements Runnable {

	double dt;

	Thread thread;

	public Simulation(double dt) {
		super();
		this.dt = dt;

		this.thread = new Thread(this);
	}

	public void start() {
		initialize();
		thread.start();
	}

	@Override
	public void run() {
		while (Thread.currentThread() == thread) {
			tick();
			try {
				Thread.sleep((long) (dt * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract void initialize();

	protected abstract void tick();

}
