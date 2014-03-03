package core;
import java.util.concurrent.LinkedBlockingQueue;
//TODO: On a setInterval, check the states of transactions so they can update their respective tracks.

//Every actor needs to have a transaction data structure as well,
//so they can have a history of previous transactions
//Also has the added benefit of letting me see when Transaction states have changed so we can update the GUI.
public class Actor {
	public final String name;
	protected LinkedBlockingQueue<Transaction> transactions;
	protected LinkedBlockingQueue<Transaction> globalTrans;//This gets synced with the global transactions
	protected LinkedBlockingQueue<Transaction> offers;
	public Actor(String sentName, LinkedBlockingQueue <Transaction> queue){
		name = sentName;
		globalTrans = queue;
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
	public void acceptOffer(Transaction accept){
		try{
			globalTrans.add(accept);
			accept.setState(Transaction.STATE_SUBMITTED);
		}finally{
			if(accept.getState() == Transaction.STATE_ACCEPTED){
				//Do your accept actions
			}else if (accept.getState() == Transaction.STATE_INVALID){
				//Tell Actors that their transaction didn't go through for various reasons
			}
			
		}
	}
	//If the Actor declines, we set the state to declined and remove it from their list of offers
	//but leave it in their transaction history
	public void declineOffer(Transaction decline){
		try{
			removeFromOffers(decline);
		}finally{
			decline.setState(Transaction.STATE_DECLINE);
		}
	}
	/**
	 * Remove from Offer List
	 * @param offer- Transaction to remove from queue
	 */
	private void removeFromOffers(Transaction offer) {
		// TODO Auto-generated method stub
		for(Transaction o : offers){
			if(o.equals(offer)){
				offers.remove(o);
				break;
			}
		}
	}

	/**
	 * Quite a useless method. 
	 * Just a stub, for if we need some exit code to occur for deferring like moving it to the back of the queue.
	 * @return Nothing for now.
	 */
	public void deferOffer(){}

}
