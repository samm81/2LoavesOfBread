package core.channels;

import core.Offer;
import core.actors.Actor;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Brian Oluwo
 *         Created on 4/22/14.
 */
public class Consumer extends Thread{
    protected HashSet<Actor> actors;
    protected HashMap<Actor,Offer> offers;
    protected double dt;
    protected boolean update;
    public Consumer(HashSet<Actor> actors, HashMap<Actor,Offer> offers, double dt, boolean update){
        this.actors = actors;
        this.offers = offers;
        this.dt = dt;
        this.update = update;
    }
    @Override
    public void run(){
        while(update) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        consume();
        update = true;
        try {
            Thread.yield();
        } finally {
            try {
                Thread.sleep((long) (this.dt * 1000));
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume() {

    }
}
