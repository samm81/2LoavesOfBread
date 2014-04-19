package core;

import core.actors.Actor;
import core.commodities.Commodity;

/**
 * New structure for Actors to barter with.
 * 
 * @author Brian Oluwo
 */
public class Offer {
	
	public final Actor sender;
	protected final Commodity commodity1;
	protected final Commodity commodity2;
	private final int maxTradeVolume;
	private final int minReceive;
	
	public Offer(Commodity commodity1, Commodity commodity2, int maxTradeVolume, int minReceive, Actor sender) {
		this.commodity1 = commodity1;
		this.commodity2 = commodity2;
		this.maxTradeVolume = maxTradeVolume;
		this.minReceive = minReceive;
		this.sender = sender;
	}
	
	public Offer(Transaction t, int maxTradeVolume) {
		this.commodity1 = t.getCommodity1();
		this.commodity2 = t.getCommodity2();
		this.minReceive = t.getVolume2();
		this.sender = t.getSender();
		this.maxTradeVolume = maxTradeVolume;
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
	
	public int getMaxTradeVolume() {
		return maxTradeVolume;
	}
	
	public int getMinReceive() {
		return minReceive;
	}
}
