package core;

import core.actors.Actor;
import core.commodities.Commodity;

/**
 * Class to hold all offers, past, present and future, in terms
 * of what is being traded and at what values.
 * Primarily a data class.
 * 
 * @author Brian Oluwo
 * 
 */
public class Transaction {

	//These states act as a signal as to where each transaction is at in terms of processing.
	//We may want to explore a Response Class for extra robustness
	public static final double tranSlippage = 1d;
	public Commodity commodity1;
	public Commodity commodity2;
	public double volume1;
	public double volume2;
	public final Actor sender;
	public boolean processed = false;

	public Transaction(double volume1, Commodity commodity1, double volume2, Commodity commodity2, Actor sender) {
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;
		this.volume1 = volume1;
		this.volume2 = volume2;
		this.sender = sender;
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
	//TODO: Do we need to handle possible overflow?
	public double getRatio() {
		assert volume1 > 0 : "Volume 1 was less than or equal to 0!";
		assert volume2 > 0 : "Volume 2 was less than or equal to 0!";
		if(volume1 == 0){
			return 1;
		}
		// TODO: Sometimes we get divide by zero errors. Should probably figure out why sometime.
		return volume2 / volume1;
	}

	public double getTotalVolume() {
		return volume1 + volume2;
	}
	public Actor getSender(){
		return this.sender;
	}
	public boolean getState (){
		return this.processed;
	}
	public void setState (boolean bool){
		this.processed = bool;
	}
	public Transaction getReversedTransaction() {
		return new Transaction(this.volume2, this.commodity2, this.volume1, this.commodity1,this.sender);
	}

	public boolean equals(Transaction e) {
		if(this.commodity1.name().equals(e.getCommodity1().name()) 
				&& this.commodity2.name().equals(e.getCommodity2().name())
				&& this.getRatio() <= (e.getRatio() + Transaction.tranSlippage) 
				&& this.getRatio() >= (e.getRatio() - Transaction.tranSlippage)
				){
			return true;
		}
		return false;
	}
	@Override
	public String toString() {
		return volume1 + " " + commodity1.name() + " for " + volume2 + " " + commodity2.name();

	}
}
