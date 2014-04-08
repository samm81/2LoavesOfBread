package core;

/**
 * Abstract class to encapsulate any simulation.
 *
 * @author Sam Maynard
 */
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
        this.thread.start();
    }

    @Override
    public void run() {
        while (Thread.currentThread() == this.thread) {
            tick();
            try {
                Thread.sleep((long) (this.dt * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void initialize();

    protected abstract void tick();

}
