package core;

import java.util.UUID;

import core.actors.Actor;
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
	public static final double tranSlippage = 1d;
	public final UUID id;
	public Commodity commodity1;
	public Commodity commodity2;
	public int volume1;
	public int volume2;
	public final Actor sender;

	public Transaction(int volume1, Commodity commodity1, int volume2, Commodity commodity2, Actor sender) {
		this.id = UUID.randomUUID();
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;
		this.volume1 = volume1;
		this.volume2 = volume2;
		this.sender = sender;
	}

	public UUID getID() {
		return this.id;
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
	public Actor getSender(){
		return this.sender;
	}

	public Transaction getReversedTransaction() {
		return new Transaction(this.volume2, this.commodity2, this.volume1, this.commodity1,this.sender);
	}
	public boolean compareID (UUID id){
		if(this.id.equals(id)){
			return true;
		}
		return false;
	}
	public boolean equals(Transaction e) {
		if(this.commodity1.name().equals(e.getCommodity1().name()) 
				&& this.commodity2.name().equals(e.getCommodity2().name())
				&& this.getVolume1() <= (e.getVolume1() + Transaction.tranSlippage) 
				&& this.getVolume1() >= (e.getVolume1() - Transaction.tranSlippage)
				&& this.getVolume2() <= (e.getVolume2() + Transaction.tranSlippage) 
				&& this.getVolume2() >= (e.getVolume2() - Transaction.tranSlippage)
				){
			return true;
		}



		return false;

	}
}
