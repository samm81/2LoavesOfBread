package core.channels;

import core.Offer;
import core.actors.Actor;
import core.commodities.Commodity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Brian Oluwo
 *         Created on 4/22/14.
 */
public class Producer extends Thread {
    protected HashSet<Actor> actors;
    protected HashMap<Actor, Offer> offers;
    protected double dt;
    protected boolean update;

    public Producer(HashSet<Actor> actors, double dt) {
        this.actors = actors;
        this.dt = dt;
        this.offers = new HashMap<Actor, Offer>();
        this.update = true;
    }

    @Override
    public void run() {
        while (!update) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        produce();
        update = false;
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

    private void produce() {
        for (Actor actor : this.actors) {
            Offer offer = actor.getBestOffer();
            offers.put(actor, offer);
        }
    }

    /**
     * @param commodity - The commodity to be inquired about.
     * @return - # of Offers that offer commodity as their trading away commodity
     */
    public int getNumberOfOffers(Commodity commodity) {
        Collection<Offer> offerCollection = this.offers.values();
        int count = 0;
        for (Offer offer : offerCollection) {
            if (offer.getCommodity1().equals(commodity))
                count++;
        }
        return count;
    }

    /**
     * Specific for between two commodities. Takes longer.
     *
     * @param tradeAway - Trading Away Commodity
     * @param tradeFor  - Trading For Commodity
     * @return - Number of Trades trading away tradeAway, for tradeFor
     */
    public int getNumberOfOffers(Commodity tradeAway, Commodity tradeFor) {
        Collection<Offer> offerCollection = this.offers.values();
        int count = 0;
        for (Offer offer : offerCollection) {
            if (offer.getCommodity1().equals(tradeAway) && offer.getCommodity2().equals(tradeFor))
                count++;
        }
        return count;
    }
}
