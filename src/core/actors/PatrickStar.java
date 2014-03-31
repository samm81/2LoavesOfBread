package core.actors;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import core.Transaction;
import core.commodities.Commodity;

public class PatrickStar extends Actor {

	public PatrickStar(LinkedList<Commodity> commodities,
			LinkedBlockingQueue<Transaction> transaction) {
		super(commodities, transaction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Transaction getBestOffer() {
		int want = (int) (Math.random()*this.commodities.size()); //item wanted
		int tradedaway = -1; //item to be traded for want

		int[] inven = new int[commodities.size()];
		for(int i = 0; i < inven.length; i++)
		{
			inven[i] = volumes.get(commodities.get(i));
		}


		for(int i = 0; i < exchangematrix[want].length; i++)
		{
			if(tradedaway == -1)
				tradedaway = i;
			else
			{
				if(inven[i]/exchangematrix[want][i] > inven[tradedaway]/exchangematrix[want][tradedaway])
					tradedaway = i;
			}
		}

		int vol1 = (int) Math.ceil(inven[tradedaway]/2);
		int vol2 = (int) (vol1 * exchangematrix[want][tradedaway]);

		/*try {
			submitTransaction(commodities.get(tradedaway), commodities.get(want), vol1, vol2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return new Transaction(vol1, commodities.get(tradedaway), vol2, commodities.get(want), this);
	}

	@Override
	public void evaluateMarket() {
		Iterator<Commodity> i = this.commodities.iterator();
		int col = 0;
		while(i.hasNext()){
			Commodity a = i.next();
			Hashtable<String, Double> exchangerate = a.getMostRecentRatios();

			for(int row = 0; row < exchangematrix.length; row++)
			{
				if(row != col)
					exchangematrix[row][col] = Math.abs((exchangematrix[row][col] + exchangerate.get(a.name()) / 2) + Math.random());
				else
					exchangematrix[row][col] = 1;
			}

			col++;
		}
	}

}
