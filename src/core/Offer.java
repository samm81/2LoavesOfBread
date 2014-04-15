package core;

import core.actors.Actor;
import core.commodities.Commodity;

/**
 * New structure for Actors to barter with.
 * @author Brian Oluwo
 */
public class Offer {

    public final Actor sender;
    protected final Commodity commodity1;
    protected final Commodity commodity2;
    private double rate;
    private int maxTradeVolume;
    private int minReceive;

    public Offer(Commodity commodity1, Commodity commodity2, double rate, int maxTradeVolume, int minReceive, Actor sender) {
        this.commodity1 = commodity1;
        this.commodity2 = commodity2;
        this.setRate(rate);
        this.setMaxTradeVolume(maxTradeVolume);
        this.setMinReceive(minReceive);
        this.sender = sender;
    }
    public Offer(Transaction t, int maxTradeVolume){
        this.commodity1 = t.getCommodity1();
        this.commodity2 = t.getCommodity2();
        this.setRate(t.getRatio());
        this.setMaxTradeVolume(maxTradeVolume);
        this.setMinReceive(t.getVolume2());
        this.sender = t.getSender();
    }

    public Commodity getCommodity1() {
        return this.commodity1;
    }

    public Commodity getCommodity2() {
        return this.commodity2;
    }

    public Actor getSender() {
        return this.sender;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getMaxTradeVolume() {
        return maxTradeVolume;
    }

    public void setMaxTradeVolume(int maxTradeVolume) {
        this.maxTradeVolume = maxTradeVolume;
    }

    public int getMinReceive() {
        return minReceive;
    }

    public void setMinReceive(int minReceive) {
        this.minReceive = minReceive;
    }
}
