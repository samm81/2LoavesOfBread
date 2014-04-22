package core.channels;

import core.Offer;

/**
 * Tag-along Errand Boy
 * @author Brian Oluwo
 *         Created on 4/22/14.
 */
public class ErrandBoy {
    protected int index;
    public ErrandBoy(int indexInArrayList){
        this.index = indexInArrayList;
    }


    /**
     * Conditions to be checked:
     * 1. They get at least minReceive
     * 2. They give away no more than maxOffer
     *
     * @param first - The original offer which is checking for viability against offer 2.
     * @param second - The offer to be compared against
     * @return Whether or not this offer should be processed
     */
    public boolean isViable(Offer first, Offer second) {
        // No longer is there a reverse transaction so one needs to check reverses.
        if(first == null || second == null)
            return false;
        if(!first.getCommodity1().name().equals(second.getCommodity2().name()) || !first.getCommodity2().name().equals(second.getCommodity1().name()))
            return false;
        return !(first.getMinReceive() > second.getMaxTradeVolume() || second.getMinReceive() > first.getMaxTradeVolume());

    }
}
