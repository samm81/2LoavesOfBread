package core.actors;

import core.Transaction;
import core.commodities.Commodity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract class the represents every player.
 *
 * @author Patrick Shan
 */

public abstract class Actor {

    final Integer startingVolumes;
    protected LinkedList<Commodity> commodities;
    protected LinkedBlockingQueue<Transaction> transactions;
    protected double[][] exchangematrix; //actor's own personal exchange rate
    //TODO: Currently Unused?
    protected double[] wantmatrix; //what the actor wants and what they are willing to trade for.
    protected ConcurrentHashMap<Commodity, Integer> volumes;

    public Actor(LinkedList<Commodity> commodities, LinkedBlockingQueue<Transaction> transaction) {
        this.commodities = commodities;
        this.transactions = transaction;

        this.volumes = new ConcurrentHashMap<>(this.commodities.size());
        this.exchangematrix = new double[commodities.size()][commodities.size()];

        //random values to start with
        for (int row = 0; row < exchangematrix.length; row++) {
            for (int col = 0; col < exchangematrix[row].length; col++) {
                exchangematrix[row][col] = Math.random();
            }
        }
        this.startingVolumes = 3;
        for (Commodity s : this.commodities)
            this.volumes.put(s, this.startingVolumes);
        this.exchangematrix = new double[commodities.size()][commodities.size()];
    }

    public Transaction getBestOffer() {
        int want = (int) (Math.random() * this.commodities.size()); //item wanted
        int tradedaway = -1; //item to be traded for want
        int[] inven = new int[this.commodities.size()];
        for (int i = 0; i < inven.length; i++) {
            inven[i] = volumes.get(this.commodities.get(i));
        }
        for (int i = 0; i < exchangematrix[want].length; i++) {
            if (tradedaway == -1)
                tradedaway = i;
            else {
                if (inven[i] / exchangematrix[want][i] > inven[tradedaway] / exchangematrix[want][tradedaway])
                    tradedaway = i;
            }
        }
        int vol1 = (int) Math.ceil(inven[tradedaway] / 2);
        int vol2 = (int) (vol1 * exchangematrix[want][tradedaway]);
        return new Transaction(vol1, this.commodities.get(tradedaway), vol2, this.commodities.get(want), this);
    }

    /*
    For MVP: Selects a random thing and then picks an offer that they can actually make.
    If they cannot afford making any offers it picks another random offer.
    patrick:
    actor should look at their goods, their wants, and the market
    then reevaluate how much they are willing to trade for each object
    This method will simply average the exchange rate and the new ratio for MVP. Then add/subtract a random amount.
    */

    public void evaluateMarket() {
//		System.out.println("EVALUATING MARKETS! :D");
        Iterator<Commodity> i = this.commodities.iterator();
        int col = 0;
        boolean pass = false;
        Commodity a = Commodity.Bread;
        while (i.hasNext()) {
            HashMap<String, Double> exchangeRate = a.getMostRecentRatios();
            for (Commodity z : Commodity.values()) {
                if (!pass) {
                    pass = true;

                } else {
                    exchangeRate.putAll(z.getMostRecentRatios());
                    break;
                }
            }
            for (int row = 0; row < exchangematrix.length && col < exchangematrix[row].length; row++) {
                if (exchangeRate.get(a.name()) != null) {
                    if (row != col)
                        exchangematrix[row][col] = Math.abs((exchangematrix[row][col] + exchangeRate.get(a.name()) / 2) + Math.random());
                    else
                        exchangematrix[row][col] = 1;
                }
            }
            col++;
            a = i.next();
        }
    }

    public void acceptTransaction(Transaction t) {
        if (this.volumes.get(t.commodity1) - t.volume1 > 0) {
            this.volumes.put(t.getCommodity1(), (int) (this.volumes.get(t.getCommodity1()) + t.getVolume1()));
            this.volumes.put(t.getCommodity2(), (int) (this.volumes.get(t.getCommodity2()) + t.getVolume2()));
        }

    }
}

/*
 * This is code that will pick an offer in the list that best matches the wants and needs.
 * It does not account for whether or not they can make that transaction though.
 * 
 * Transaction t = null;
 * 
 * Iterator<Transaction> i = transactions.iterator();
 * while(i.hasNext())
 * {
 * if(t == null)
 * {
 * t = i.next();
 * }
 * else
 * {
 * Transaction t2 = i.next();
 * double currRatio = t.getRatio();
 * double newRatio = t2.getRatio();
 * t = (newRatio > currRatio)?t2:t;
 * 
 * }
 * }
 * 
 * if(t != null)
 * {
 * acceptTransaction(t);
 * }
 */