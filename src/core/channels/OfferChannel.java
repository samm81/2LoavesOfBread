package core.channels;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import core.*;
//TODO: Have this extend a generic Channel class
/**
 * @author Brian Oluwo
 * {@link #OfferChannel(BlockingQueue, HashSet)} -
 * {@link #run()}-Run
 *  This is the channel that directs offers to the appropriate actors queue
 */
public class OfferChannel implements Runnable{

    protected ArrayBlockingQueue<Transaction> offers;
    public OfferChannel(BlockingQueue<Transaction> queue) {
        this.offers = (ArrayBlockingQueue<Transaction>) queue;
    }
//Adds to records of both actors, then take it off queue
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