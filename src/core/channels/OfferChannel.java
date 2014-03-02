package core.channels;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import core.*;
//TODO: Have this extend a generic Channel class
/**
 * @author Brian Oluwo
 * Offer Channel class
 * ########################
 * {@link #OfferChannel(BlockingQueue, HashSet)} -
 * {@link #run()}-Run
 *  
 */
public class OfferChannel implements Runnable{

    protected BlockingQueue<Offer> offers = null;
    protected HashSet<Actor> actors;
    public OfferChannel(BlockingQueue<Offer> queue,HashSet<Actor> set) {
        this.offers = queue;
        this.actors = set;
    }

    public void run() {
        try {
        	offers.notify();
           if(!offers.isEmpty()){
        	   //Go ahead and do your thing
        	   
           }
           else{
        	   offers.wait();
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}