package core;

import core.commodities.Commodity;

/**
 * Class to hold all offers, past, present and future, in terms
 * of what is being traded and at what values.
 * Primarily a data class.
 *
 * @author Brian Oluwo
 */
public class Transaction {

    public Commodity commodity1;
    public Commodity commodity2;
    public int volume1;
    public int volume2;

    public Transaction(int volume1, Commodity commodity1, int volume2, Commodity commodity2) {
        this.commodity1 = commodity1;
        this.commodity2 = commodity2;
        this.volume1 = volume1;
        this.volume2 = volume2;
    }

    public Commodity getCommodity1() {
        return this.commodity1;
    }

    public Commodity getCommodity2() {
        return this.commodity2;
    }

    public int getVolume1() {
        return this.volume1;
    }

    public int getVolume2() {
        return this.volume2;
    }


    public double getRatio() {
        assert this.volume1 > 0 : "Volume 1 was less than or equal to 0!";
        assert this.volume2 > 0 : "Volume 2 was less than or equal to 0!";

        return ((double)this.volume2 / (double)this.volume1);
    }

    public Transaction getReversedTransaction() {
        return new Transaction(this.volume2, this.commodity2, this.volume1, this.commodity1);
    }

    @Override
    public String toString() {
        return this.volume1 + " " + this.commodity1.name() + " for " + this.volume2 + " " + this.commodity2.name();

    }
}
