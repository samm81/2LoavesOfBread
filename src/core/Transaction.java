package core;

import core.commodities.Commodity;

/**
 * Class that encapsulates completed/viability pending Offers.
 * Primarily a data class.
 * @author Brian Oluwo
 */
public class Transaction {

    public Commodity commodity1;
    public Commodity commodity2;
    public int volume1;
    public int volume2;

    /**
     * Constructor
     * @param volume1 - Volume of Commodity1 being traded Away
     * @param commodity1 - Commodity being traded away
     * @param volume2 - Volume of Commodity2 being Traded Away
     * @param commodity2 - Commodity being traded for
     */
    public Transaction(int volume1, Commodity commodity1, int volume2, Commodity commodity2) {
        this.commodity1 = commodity1;
        this.commodity2 = commodity2;
        this.volume1 = volume1;
        this.volume2 = volume2;
    }

    /**
     *
     * @return The Commodity being traded away
     */
    public Commodity getCommodity1() {
        return this.commodity1;
    }

    /**
     *
     * @return The Commodity being traded for
     */
    public Commodity getCommodity2() {
        return this.commodity2;
    }

    /**
     *
     * @return The Volume of Commodity1 being traded away
     */
    public int getVolume1() {
        return this.volume1;
    }

    /**
     *
     * @return The Volume of Commodity2 being traded for
     */
    public int getVolume2() {
        return this.volume2;
    }

    /**
     *
     * @return The ratio of traded for to traded away. Used to update tickers most likely.
     */
    public double getRatio() {
        assert this.volume1 > 0 : "Volume 1 was less than or equal to 0!";
        assert this.volume2 > 0 : "Volume 2 was less than or equal to 0!";

        return ((double)this.volume2 / (double)this.volume1);
    }

    /**
     *
     * @return - The opposite of the current transaction
     */
    public Transaction reverse() {
        return new Transaction(this.volume2, this.commodity2, this.volume1, this.commodity1);
    }

    /**
     *
     * @return - Prettified String output, signifying the data of this Transaction
     */
    @Override
    public String toString() {
        return this.volume1 + " " + this.commodity1.name() + " for " + this.volume2 + " " + this.commodity2.name();
    }

    /**
     *
     * @return- HashCode unique to the specified data in this transaction.
     */
    @Override
    public int hashCode() {
    	return commodity1.ordinal() * 1000000 + volume1 * 10000 + commodity2.ordinal() * 100 + volume2;
    }

    /**
     *
     * @param obj - An object to be checked against
     * @return - Whether or not the object and the Transaction are equivalent.
     */
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Transaction && obj.hashCode() == hashCode();
    }
    
}
