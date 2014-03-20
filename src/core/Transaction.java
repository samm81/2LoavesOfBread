package core;

import java.util.UUID;

import core.commodities.Commodity;

/**
 * Class to hold all transactions, past, present and future, in terms
 * of what is being traded and at what values.
 * Primarily a data class.
 * 
 * @author Brian Oluwo
 * 
 */
public class Transaction {
	
	//These states act as a signal as to where each transaction is at in terms of processing.
	//We may want to explore a Response Class for extra robustness
	
	public static final int STATE_SUBMITTED = 0;
	public static final int STATE_PENDING = 1;
	public static final int STATE_ACCEPTED = 2;
	public static final int STATE_INVALID = 3;
	public static final int STATE_OFFER = 4;
	public static final int STATE_DECLINE = 5;
	public final UUID id;
	public Commodity commodity1;
	public Commodity commodity2;
	protected int state;
	public double volume1;
	public double volume2;
	
	public Transaction(double volume1, Commodity commodity1, double volume2, Commodity commodity2) {
		this.id = UUID.randomUUID();
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;
		this.volume1 = volume1;
		this.volume2 = volume2;
		this.state = STATE_OFFER;
	}
	
	public UUID getID() {
		return this.id;
	}
	
	public int getState() {
		return this.state;
	}
	
	public Commodity getCommodity1() {
		return commodity1;
	}
	
	public Commodity getCommodity2() {
		return commodity2;
	}
	
	public double getVolume1() {
		return volume1;
	}
	
	public double getVolume2() {
		return volume2;
	}
	
	public double getRatio() {
		return volume2 / volume1;
	}
	
	public double getTotalVolume() {
		return volume1 + volume2;
	}
	
	public void setState(int newState) {
		this.state = newState;
	}
	
	public Transaction getReversedTransaction() {
		return new Transaction(volume2, commodity2, volume1, commodity1);
	}
	
	public boolean equals(Transaction e) {
		if(this.getID() == e.getID())
			return true;
		return false;
		
	}
}
