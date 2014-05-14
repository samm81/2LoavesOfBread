package core.channels;

import core.Offer;
import core.Transaction;
import core.actors.Actor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Brian Oluwo
 *         Created on 4/22/14.
 */
public class TaskMaster extends Thread{
    protected HashSet<Actor> actors;
    protected HashMap<Actor,Offer> offers;
    protected LinkedBlockingQueue<Transaction> globalTransactions;
    protected double dt;
    protected boolean update;
    private Offer[] offersArray;
    public TaskMaster(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, HashMap<Actor, Offer> offers, double dt, boolean update, int numActors){
        this.globalTransactions = globalTransactions;
        this.actors = actors;
        this.offers = offers;
        this.dt = dt;
        this.update = update;
        this.offersArray = new Offer[numActors+1];
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
    /**
     * Conditions to be checked:
     * 1. They get at least minReceive
     * 2. They give away no more than maxOffer
     *
     * @param first  - The original offer which is checking for viability against offer 2.
     * @param second - The offer to be compared against
     * @return Whether or not this offer should be processed
     */
    public boolean isViable(Offer first, Offer second) {
        // No longer is there a reverse transaction so one needs to check reverses.
        if(first == null || second == null)
            return false;
        if(!first.getCommodity1().name().equals(second.getCommodity2().name()) || !first.getCommodity2().name().equals(second.getCommodity1().name()))
            return false;
        return !(first.getMinReceive() > second.getMaxTradeVolume() || (second.getMinReceive() > first.getMaxTradeVolume()));

    }

    private void consume() {
        int count = 0;
        //Populate Array With Offers
        for(Map.Entry<Actor, Offer> entry : offers.entrySet()) {
            offersArray[count] = entry.getValue();
            count++;
        }

        //ArrayList<Offer> offers = Collections.list(Collections.enumeration(offersMap.values()));
        //		Collections.shuffle(offers);
        for(int i = 0; i < offersArray.length; i++) {
            Offer first = offersArray[i];
            for(int j = i + 1; j < offersArray.length; j++) {
                Offer second = offersArray[j];
                if(isViable(first, second)) {
                    acceptOffers(first, second);

                    //					offers.remove(j);
                    //System.out.println("Matched offer " + first + " with " + second);
                    break;
                }
            }
        }
    }
    public void acceptOffers(Offer first, Offer second) {
        Transaction t = new Transaction(first.getMinReceive(), first.getCommodity2(), second.getMinReceive(), first.getCommodity1());
        Transaction q = new Transaction(second.getMinReceive(), second.getCommodity2(), first.getMinReceive(), second.getCommodity1());

        first.getSender().acceptTransaction(t);
        second.getSender().acceptTransaction(q);

        t.getCommodity1().addTransaction(t);
        t.getCommodity2().addTransaction(q);
        try {
            this.globalTransactions.put(t);
            this.globalTransactions.put(q);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        offers.remove(first.getSender());
        offers.remove(second.getSender());
    }
}
