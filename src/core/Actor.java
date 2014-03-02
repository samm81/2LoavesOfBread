package core;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//Every actor needs to have a transaction data structure as well,
//so they can have a history of previous transactions
//Also has the added benefit of letting me say when 
public class Actor {
	public final String name;
	protected BlockingQueue<Offer> offers;
	public Actor(String sentName){
		name = sentName;
		offers = new ArrayBlockingQueue<Offer>(10);
	}

	public String getName(){
		return name;
	}

	public void reevaluateWeights() {
		// TODO Auto-generated method stub

	}

	public Offer getBestOffer() {
		// TODO Auto-generated method stub
		return null;
	}

	public void evaluateMarket() {
		// TODO Auto-generated method stub

	}

}
