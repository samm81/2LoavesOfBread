package core.channels;

import core.Offer;
import core.Transaction;

import java.util.concurrent.RecursiveTask;

/**
 * Tag-along Errand Boy
 * @author Brian Oluwo
 *         Created on 4/22/14.
 */
public class ErrandBoy extends RecursiveTask {
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

        offewrs.remove(first.getSender());
        offers.remove(second.getSender());
    }

    @Override
    protected int [] compute() {
        int matched = 0;
        return null;
        return new int[index,matched];//
    }
}
