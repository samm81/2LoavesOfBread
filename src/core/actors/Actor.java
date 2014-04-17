package core.actors;

import core.Offer;
import core.Transaction;
import core.commodities.Commodity;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract class the represents every player.
 *
 * @author Patrick Shan
 */
public class Actor {

    protected LinkedList<Commodity> commodities;
    protected LinkedBlockingQueue<Transaction> transactions;
    private double[][] exchangeMatrix;
    private double[] wantMatrix;
    private double[] priorityMatrix;
    private ConcurrentHashMap<Commodity, Integer> volumes;

    private int[] initialValues;
    protected Offer bestOffer;


    /**
     * Constructor
     *
     * @param startingVolumes
     * @param priorities
     */
    public Actor(LinkedList <Commodity> commodities, LinkedBlockingQueue<Transaction> transactions, int[] startingVolumes, double[] priorities)
    {
        this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
        this.exchangeMatrix = new double[startingVolumes.length][startingVolumes.length];
        this.initialValues = startingVolumes;
        this.priorityMatrix = priorities;
        this.commodities = commodities;
        this.transactions = transactions;

        //exchange matrix setup.
        for(int row = 0; row < exchangeMatrix.length; row++) {
            for(int col = 0; col < exchangeMatrix[row].length; col++) {
                exchangeMatrix[row][col] = Math.random();
            }
        }
        int i = 0;
        //this.commodities has the same order as Commodity.values();
        //sets up the starting inventory
        for(Commodity s : this.commodities)
        {
            this.volumes.put(s, initialValues[i]);
            i++;
        }

    }

    /**
     * Get Best Offer returns a transaction that the actor will submit based on
     * an actor's want matrix and exchange matrix.
     *
     * @return Transaction that the actor will submit.
     */
    public Offer getBestOffer(){
        int want = (int) (Math.random() * this.commodities.size()); //item wanted
        int tradedAway = -1; //item to be traded for want
        int[] inventory = new int[this.commodities.size()];
        for(int i = 0; i < inventory.length; i++) {
            inventory[i] = volumes.get(this.commodities.get(i));
        }
        for(int i = 0; i < exchangeMatrix[want].length; i++) {
            if(tradedAway == -1)
                tradedAway = i;
            else {
                if(inventory[i] / exchangeMatrix[want][i] > inventory[tradedAway] / exchangeMatrix[want][tradedAway])
                    tradedAway = i;
            }
        }
        int vol1 = (int) Math.ceil(inventory[tradedAway] / 2);
        int vol2 = (int) (vol1 * exchangeMatrix[want][tradedAway]);
        if (vol1 <= 0 || vol2 <= 0 || tradedAway == want)
            return null;
        bestOffer = new Offer(new Transaction(vol1, this.commodities.get(tradedAway), vol2, this.commodities.get(want), this), vol1);
        return bestOffer;
    }

    /**
     * EvaluateMarket is how an actor will change the values in the market's exchange rates
     * for various goods. It represents the ability of people to recognize changing prices.
     */
    public void evaluateMarket() {
        int col;
        int row = 0;
        //change the exchange matrix.
        //Exchange Matrix is set up so
        //row and column 0 is one commodity, row, column 1 is one commodity ... etc.
        //this for loop shows the movement of markets
        for (Commodity x : Commodity.values()) {
            col = 0;
            for (Commodity y : Commodity.values()) {
                if (y == x)
                    exchangeMatrix[row][col] = 1;
                else if (x.getMostRecentRatios().get(y.name()) != null) {
//						System.out.println(x.name() + " " + y.name() +"\n");
//						System.out.println(row + " " + col);
//						System.out.println(x.getMostRecentRatios().get(y.name()));
						exchangeMatrix[row][col] = exchangeMatrix[row][col];
					}
					else
					{
						exchangeMatrix[row][col] = exchangeMatrix[row][col];
					}
					col++;
				}
				row++;
			}
	}
	
	/**
	 * AcceptTransaction changes actor inventories as they perform transaction t.
	 * @param t the transaction to be performed
	 */
	public boolean acceptTransaction(Transaction t) {
		if(this.volumes.get(t.commodity1) - t.volume1 > 0 && t.commodity1 != t.commodity2 && t.volume1 != 0 && t.volume2 != 0) {
			this.volumes.put(t.getCommodity1(), this.volumes.get(t.getCommodity1()) + t.getVolume1());
			this.volumes.put(t.getCommodity2(), this.volumes.get(t.getCommodity2()) + t.getVolume2());
			System.out.println("Trade made " + t.volume1 + " " + t.commodity1.name() + " for " + t.volume2 + " " + t.commodity2.name());
            return true;
        }
        return false;
    }
}