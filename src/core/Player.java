package core;


public class Player {
	
	Transaction bestOffer = null;
	
	public Player() {}
	
	public Transaction getBestOffer() {
		return bestOffer;
	}
	
	public void setBestOffer(Transaction bestOffer) {
		this.bestOffer = bestOffer;
	}
	
}
