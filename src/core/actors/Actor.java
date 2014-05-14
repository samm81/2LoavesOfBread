package core.actors;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import core.Offer;
import core.Transaction;
import core.channels.OfferChannel;
import core.commodities.Commodity;

/**
 * Abstract class the represents every player.
 * 
 * @author Patrick Shan
 */
public abstract class Actor {
	
	protected List<Commodity> commodities;
	protected LinkedBlockingQueue<Transaction> transactions;
	private HashMap<Commodity, Integer> needMatrix;
	protected double[][] exchangeMatrix;
	protected double[] inventoryVal;
	protected int[] priorityMatrix;
	protected ConcurrentHashMap<Commodity, Integer> volumes;
	
	private double[] marketshare;
	private int[] initialValues;
	protected Offer bestOffer;
	
	/**
	 * Constructor
	 * 
	 * @param startingVolumes
	 * @param priorities
	 */
	public Actor(List<Commodity> commodities, LinkedBlockingQueue<Transaction> transactions, int[] startingVolumes, int[] priorities) {
		this.volumes = new ConcurrentHashMap<Commodity, Integer>(startingVolumes.length);
		this.exchangeMatrix = new double[startingVolumes.length][startingVolumes.length];
		this.inventoryVal = new double[startingVolumes.length];
		this.needMatrix = new HashMap<Commodity, Integer>();
		this.initialValues = startingVolumes;
		this.priorityMatrix = priorities;
		this.commodities = commodities;
		this.transactions = transactions;
		
		//exchange matrix setup.
		for(int row = 0; row < exchangeMatrix.length; row++) {
			for(int col = 0; col < exchangeMatrix[row].length; col++) {
				exchangeMatrix[row][col] = Math.random() * 3;
			}
		}
		int i = 0;
		//this.commodities has the same order as Commodity.values();
		//sets up the starting inventory
		for(Commodity s : this.commodities) {
			this.volumes.put(s, initialValues[i]);
			i++;
		}
		for(int j = 0; j < commodities.size(); j++) {
			needMatrix.put(commodities.get(j), priorityMatrix[j] - volumes.get(commodities.get(j)));
		}

		
	}
	
	public Actor() {}
	
	/**
	 * Get Best Offer returns a transaction that the actor will submit based on
	 * an actor's want matrix and exchange matrix.
	 * 
	 * @return Transaction that the actor will submit.
	 */
	public Offer getBestOffer() {
		//set up the need matrix and find the actors total net worth in each commodity
		for(int i = 0; i < commodities.size(); i++) {
			needMatrix.put(commodities.get(i), priorityMatrix[i] - volumes.get(commodities.get(i)));
			for(int j = 0; j < commodities.size(); j++) {
				inventoryVal[i] += (exchangeMatrix[j][i] * volumes.get(commodities.get(j)));
			}
		}
		
		//find highest inventoryVal/needed
		Commodity wantComm = null;
		int want = 0;
		for(int i = 0; i < commodities.size(); i++) {
			if(wantComm == null) {
				wantComm = commodities.get(i);
				want = i;
			} else {
				if(inventoryVal[i] / needMatrix.get(commodities.get(i)) < inventoryVal[want] / needMatrix.get(wantComm)) {
					wantComm = commodities.get(i);
					want = i;
				}
			}
		}
		
		//select highest exchange item
		int tradedAway = 0;
		for(int i = 1; i < commodities.size(); i++) {
			if(exchangeMatrix[want][i] < exchangeMatrix[want][tradedAway] || tradedAway == want)
				tradedAway = i;
		}
		
		int vol1 = 1;
		while(Math.floor(vol1 * exchangeMatrix[tradedAway][want]) < needMatrix.get(wantComm) && vol1 < this.volumes.get(commodities.get(tradedAway)))
			vol1++;
		int vol2 = (int) Math.ceil(vol1 * exchangeMatrix[tradedAway][want]);
		bestOffer = new Offer(new Transaction(vol1, this.commodities.get(tradedAway), vol2, this.commodities.get(want)), this);
		//       System.out.println("Best Offer: " + vol1 + " " + commodities.get(tradedAway).name() + " for " + vol2 + " " + commodities.get(want).name());
		return bestOffer;
	}
	
	/**
	 * EvaluateMarket is how an actor will change the values in the market's exchange rates
	 * for various goods. It represents the ability of people to recognize changing prices.
	 */
	public void evaluateMarket(OfferChannel offerChannel) {
		int col;
		int row = 0;
		//change the exchange matrix.
		//Exchange Matrix is set up so
		//row and column 0 is one commodity, row, column 1 is one commodity ... etc.
		//this for loop shows the movement of markets
		marketshare = new double[commodities.size()];
		double totalComm = 0;
		for(int i = 0; i < marketshare.length; i++) {
			for(Offer o : offerChannel.getPendingOffers(commodities.get(i))) {
				marketshare[i] += o.getMaxTradeVolume();
				totalComm += o.getMaxTradeVolume();
			}
		}
		for(int i = 0; i < marketshare.length; i++) {
			
			marketshare[i] = marketshare[i] / totalComm;
		}
		int need = 0;
		for(int i = 0 ; i < commodities.size(); i++)
		{
			if(needMatrix.get(commodities.get(i)) > 0)
				need+=needMatrix.get(commodities.get(i));
		}
		for(Commodity x : Commodity.values()) {
			col = 0;
			for(Commodity y : Commodity.values()) {
				if(y == x) {
					exchangeMatrix[row][col] = 1;
				} else if(x.getMostRecentRatios().get(y.name()) != null && totalComm != 0) {
					double needRow = needMatrix.get(commodities.get(row));
					double needCol = needMatrix.get(commodities.get(col));
					if(marketshare[row] > marketshare[col])
					{
						exchangeMatrix[row][col] = exchangeMatrix[row][col] * ((1-(marketshare[row]-marketshare[col])/1.1)) + (Math.random() * 0.1 - 0.05);;
					}
					else
					{
						exchangeMatrix[row][col] = exchangeMatrix[row][col] * ((1+(marketshare[col]-marketshare[row])/0.9)) + (Math.random() * 0.1 - 0.05);
					}
					if(needCol > needRow && needCol > 0 && needRow > 0)
						exchangeMatrix[row][col]*=(1-(needCol-needRow)/need);
					else
						exchangeMatrix[row][col]*=(1+(needRow-needCol)/need);
				} else {
					exchangeMatrix[row][col] = Math.abs(exchangeMatrix[row][col] + (((Math.random() * 8) - 4)));
				}
				if(exchangeMatrix[row][col] > 12)
					exchangeMatrix[row][col] = Math.random() * 12;
				if(exchangeMatrix[row][col] < 0.084)
					exchangeMatrix[row][col] = Math.random();
				col++;
			}
			row++;
		}
		//thought();
	}
	
	public void thought() {
		System.out.println(this + " reporting for duty");
		System.out.println("Status:");
		for(Commodity commodity : commodities){
			System.out.println("I have " + volumes.get(commodity) + " " + commodity.name());
		}
		System.out.println("Valuation:");
		for(int i = 0; i < inventoryVal.length; i++) {
			System.out.println("I think my inventory is worth " + inventoryVal[i] + " " + commodities.get(i).name());
		}
		for(int i = 0; i < exchangeMatrix.length; i++) {
			System.out.print("I think that 1 " + commodities.get(i).name() + " is worth...  ");
			for(int j = 0; j < exchangeMatrix[i].length; j++) {
				System.out.format("%.2f %s   ", exchangeMatrix[i][j], commodities.get(j).name());
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * AcceptTransaction changes actor inventories as they perform transaction t.
	 * 
	 * @param t the transaction to be performed
	 */
	public boolean acceptTransaction(Transaction t) {
		if(this.volumes.get(t.commodity1) - t.volume1 > 0 && t.commodity1 != t.commodity2 && t.volume1 != 0 && t.volume2 != 0) {
			this.volumes.put(t.getCommodity1(), this.volumes.get(t.getCommodity1()) + t.getVolume1());
			this.volumes.put(t.getCommodity2(), this.volumes.get(t.getCommodity2()) - t.getVolume2());
			//			System.out.println("Trade made " + t.volume1 + " " + t.commodity1.name() + " for " + t.volume2 + " " + t.commodity2.name());
			return true;
		}
		return false;
	}
	
	public void addValues(double[][] exchangerates, double[] marketshares)
	{
		if(marketshare != null)
		{
			for(int i = 0; i < marketshares.length; i++)
			{
				marketshares[i]=marketshare[i];
				for(int j = 0; j < exchangeMatrix.length; j++)
				{
					exchangerates[i][j]+=exchangeMatrix[i][j];
				}
			}
		}
	}
}