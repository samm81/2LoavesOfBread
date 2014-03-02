package core;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//Every actor needs to have a transaction data structure as well,
//so they can have a history of previous transactions
//Also has the added benefit of letting me say when 
public class Actor {
	public final String name;
	protected BlockingQueue<Transaction> offers;
	public Actor(String sentName){
		name = sentName;
		offers = new ArrayBlockingQueue<Transaction>(10);
	}

	public String getName(){
		return name;
	}

	public void reevaluateWeights() {
		// TODO Auto-generated method stub

	}

	public Transaction getBestOffer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void evaluateMarket() {
		// TODO Auto-generated method stub

	}
	//If they accept an offer we need to send a transaction to update the resource counts of both
	//Actors as well update the global market that a trade happened at a new price.
	public Transaction acceptOffer(){
		return null;
	}
	//If the Actor declines, we 
	public Transaction declineOffer(){
		return null;
	}
	/**
	 * Basically allows 
	 * @return
	 */
	public Transaction deferOffer(){
		return null;
	}

}
