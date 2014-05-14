package core.channels;

import core.Offer;
import core.actors.Actor;
import core.commodities.Commodity;

import java.util.*;

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
        produce();
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
            if(offer != null)
                offers.put(actor, offer);
        }
    }

    public ArrayList<Offer> getPendingOffers() {
        return Collections.list(Collections.enumeration(offers.values()));
    }

    public ArrayList<Offer> getPendingOffers(Commodity commodity) {
        ArrayList<Offer> offerArrayList = new ArrayList<>();
        for(Map.Entry<Actor, Offer> entry : offers.entrySet()) {
            Offer offer = entry.getValue();
            if(offer.getCommodity1().equals(commodity))
                offerArrayList.add(offer);
        }
        return offerArrayList;
    }

    public ArrayList<Offer> getPendingOffers(Commodity tradeAway, Commodity tradeFor) {
        ArrayList<Offer> offerArrayList = new ArrayList<>();
        for(Map.Entry<Actor, Offer> entry : offers.entrySet()) {
            Offer offer = entry.getValue();
            if(offer.getCommodity1().equals(tradeAway) && offer.getCommodity2().equals(tradeFor))
                offerArrayList.add(offer);
        }
        return offerArrayList;
    }
}
