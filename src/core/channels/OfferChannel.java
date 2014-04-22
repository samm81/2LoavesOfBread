package core.channels;

import core.Offer;
import core.Transaction;
import core.actors.Actor;
import core.commodities.Commodity;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * {@link #OfferChannel(java.util.concurrent.LinkedBlockingQueue, java.util.HashSet, double, int)} - Basic Constructor for the thread.
 * {@link #run()} - Calls tick, then yields to main thread
 * {@link #tick()} - Every dt it gets all the new offers from the actors, and then tries to match them.
 * {@link #isViable(core.Offer, core.Offer)} - Method That ensures that two offers are viable with one another.
 * 
 * @author Brian Oluwo
 */
public class OfferChannel extends Thread {

    public Thread thread;
    protected HashSet<Actor> actors;
    protected LinkedBlockingQueue<Transaction> globalTransactions;
    protected HashMap<Actor, Offer> offersMap;
    private double dt;
    private Offer[] offers;

    public OfferChannel(LinkedBlockingQueue<Transaction> globalTransactions, HashSet<Actor> actors, double dt, int numActors) {
        this.globalTransactions = globalTransactions;
        this.actors = actors;
        this.dt = dt;
        this.thread = new Thread(this);
        this.offersMap = new HashMap<Actor, Offer>();
        //FIXME: +1 Corresponds to the number of actors, should be passed in or some sort of call.
        this.offers = new Offer[numActors + 1];
    }

    public HashMap<Actor, Offer> getOffersMap() {
        return this.offersMap;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            tick();
            try {
                Thread.yield();
            } finally {
                try {
                    Thread.sleep((long) (this.dt * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Run every dt to get the actor's best offers and then process them
     */
    private void tick() {
        long startTime = System.currentTimeMillis();
        for (Actor actor : this.actors) {
            Offer offer = actor.getBestOffer();
            offersMap.put(actor, offer);
        }
        processOffers();
//        System.out.println("Tick Took: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * Processes the offers in the queue
     */
    private void processOffers() {
        int count = 0;
        for (Map.Entry<Actor, Offer> entry : offersMap.entrySet()) {
            offers[count] = entry.getValue();
            count++;
        }
        //ArrayList<Offer> offers = Collections.list(Collections.enumeration(offersMap.values()));
//		Collections.shuffle(offers);
        for (int i = 0; i < offers.length; i++) {
            Offer first = offers[i];
            for (int j = i + 1; j < offers.length; j++) {
                Offer second = offers[j];
                if (isViable(first, second)) {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        if (first == null || second == null)
            return false;
        if (!first.getCommodity1().name().equals(second.getCommodity2().name()) || !first.getCommodity2().name().equals(second.getCommodity1().name()))
            return false;
        return !(first.getMinReceive() > second.getMaxTradeVolume() || (second.getMinReceive() > first.getMaxTradeVolume()));

    }

    public ArrayList<Offer> getPendingOffers() {
        return Collections.list(Collections.enumeration(offersMap.values()));
    }

    public ArrayList<Offer> getPendingOffers(Commodity commodity) {
        ArrayList<Offer> offerArrayList = new ArrayList<>();
        for (Map.Entry<Actor, Offer> entry : offersMap.entrySet()) {
            Offer offer = entry.getValue();
            if (offer.getCommodity1().equals(commodity))
                offerArrayList.add(offer);
        }
        return offerArrayList;
    }

    public ArrayList<Offer> getPendingOffers(Commodity tradeAway, Commodity tradeFor) {
        ArrayList<Offer> offerArrayList = new ArrayList<>();
        for (Map.Entry<Actor, Offer> entry : offersMap.entrySet()) {
            Offer offer = entry.getValue();
            if (offer.getCommodity1().equals(tradeAway) && offer.getCommodity2().equals(tradeFor))
                offerArrayList.add(offer);
        }
        return offerArrayList;
    }
}