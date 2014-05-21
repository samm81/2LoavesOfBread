package core;

import core.actors.Actor;
import core.commodities.Commodity;

/**
 * When an Actor wants to trade they submit an offer. This is the primary data class that encapsulates that request.
 * Transaction can be seen as a storage format for an offer.
 * Another data class.
 * @author Brian Oluwo
 */
public class Offer {
	
	public final Actor sender;
	protected final Commodity commodity1;
	protected final Commodity commodity2;
	private final int maxTradeVolume;
	private final int minReceive;

    /**
     * Constructor
     * @param commodity1 - Commodity Being traded away
     * @param commodity2 - Commodity being traded for
     * @param maxTradeVolume - The max amount of volume the actor is willing to trade of commodity1.
     * @param minReceive - The minimum amount of volume they are willing to receive of commodity2.
     * @param sender - The Actor who sends the offer
     */
	public Offer(Commodity commodity1, Commodity commodity2, int maxTradeVolume, int minReceive, Actor sender) {
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;
		this.maxTradeVolume = maxTradeVolume;
		this.minReceive = minReceive;
		this.sender = sender;
	}

    /**
     * A vestige of when transaction was still the only way to submit offers for evaluation.
     * Constructor for backwards compatibility
     * @param t - A transaction holding the necessary data to make the Offer.
     * @param sender - The actor who sent the offer.
     */
	public Offer(Transaction t, Actor sender) {
		this.commodity1 = t.getCommodity1();
		this.commodity2 = t.getCommodity2();
		this.maxTradeVolume = t.getVolume1();
		this.minReceive = t.getVolume2();
		this.sender = sender;
	}

    /**
     *
     * @return The commodity being traded away
     */
	public Commodity getCommodity1() {
		return this.commodity1;
	}

    /**
     *
     * @return The commodity being traded for
     */
	public Commodity getCommodity2() {
		return this.commodity2;
	}

    /**
     *
     * @return - The Actor who sent the Offer.
     */
	public Actor getSender() {
		return this.sender;
	}

    /**
     *
     * @return - The max amount of volume the actor is willing to trade of commodity1.
     */
	public int getMaxTradeVolume() {
		return maxTradeVolume;
	}

    /**
     *
     * @return - The minimum amount of volume they are willing to receive of commodity2.
     */
	public int getMinReceive() {
		return minReceive;
	}

    /**
     *
     * @return - The opposite of the current transaction
     */
	public Offer reverse(){
		return new Offer(commodity2, commodity1, minReceive, maxTradeVolume, sender);
	}

    /**
     *
     * @return - Converts Offer into a Transaction.
     */
	public Transaction toTransaction() {
		return new Transaction(maxTradeVolume, commodity1, minReceive, commodity2);
	}

    /**
     *
     * @return - Prettified String output, signifying the data of this Offer in layman's terms.
     */
	@Override
	public String toString() {
		return this.sender + "wanting to trade at most " + this.maxTradeVolume + " " + this.commodity1 + " for at least " + this.minReceive + " " + this.commodity2;
	}
}
