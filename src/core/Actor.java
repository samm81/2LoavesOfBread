package core;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Actor {
public final String name;
protected BlockingQueue offers;
	public Actor(String sentName){
		name = sentName;
		offers = new ArrayBlockingQueue(1024);
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
