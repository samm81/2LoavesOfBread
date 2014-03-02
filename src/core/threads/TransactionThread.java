package core.threads;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

import core.*;
public class TransactionThread implements Runnable{

    protected BlockingQueue<Transaction> transactions = null;
    protected HashSet<Actor> actors;
    public TransactionThread(BlockingQueue<Transaction> queue,HashSet<Actor> set) {
        this.transactions = queue;
        this.actors = set;
    }

    public void run() {
        try {
        	transactions.notify();
           if(!transactions.isEmpty()){
        	   //Go ahead and do your thing
        	   
           }
           else{
        	   transactions.wait();
           }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}