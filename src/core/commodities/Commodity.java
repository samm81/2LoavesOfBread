package core.commodities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

import core.Transaction;

public abstract class Commodity {
	
	LinkedList<Transaction> transactions;
	Hashtable<Class<?>, Graph> graphs;
	
	int graphSize = 30;
	
	public Commodity() {
		transactions = new LinkedList<Transaction>();
		graphs = new Hashtable<Class<?>, Graph>();
	}
	
	public LinkedList<Transaction> getTransactions() {
		return transactions;
	}
	
	public Hashtable<Class<?>, Graph> getGraphs() {
		return graphs;
	}
	
	public void addTransaction(Transaction transaction) {
		if(!isOrderedProperly(transaction)) {
			transaction = transaction.getReversedTransaction();
		}
		transactions.add(transaction);
		
		Commodity tradeCommodity = transaction.getCommodity2();
		if(graphs.containsKey(tradeCommodity.getClass())) {
			graphs.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		} else {
			graphs.put(tradeCommodity.getClass(), new Graph(graphSize, randomColor()));
			graphs.get(tradeCommodity.getClass()).addDataPoint(transaction.getRatio());
		}
	}
	
	private Color randomColor() {
		Random r = new Random();
		return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
	}
	
	private boolean isOrderedProperly(Transaction transaction) {
		return transaction.getCommodity1().getClass().equals(this.getClass());
	}
	
	public class Graph {
		
		LinkedList<Double> datum;
		
		double maxData = 10;
		
		Color color;
		
		final int r = 7;
		
		public Graph(int size, Color color) {
			this.color = color;
			
			datum = new LinkedList<Double>();
			for(int i = 0; i < size; i++) {
				datum.add(0d);
			}
			//findMaxData();
		}
		
		private void findMaxData() {
			for(double data : datum) {
				if(data > maxData) {
					maxData = data;
				}
			}
		}
		
		public void addDataPoint(double dataPoint) {
			double first = datum.removeFirst();
			/*
			 * if(first == maxData) {
			 * maxData = 0;
			 * findMaxData();
			 * }
			 */
			datum.add(dataPoint);
			/*
			 * if(dataPoint > maxData) {
			 * maxData = dataPoint;
			 * }
			 */
		}
		
		public void drawSelf(int x, int y, int width, int height, Graphics2D g) {
			g.setColor(color);
			
			double dx = (double) width / (double) (datum.size() - 1);
			double datax = x;
			double datay = y;
			
			for(double data : datum) {
				if(data != 0) {
					datay = y + height - ((double) height * ((double) data / maxData));
					g.fillOval((int) (datax - r / 2d), (int) (datay - r / 2d), r, r);
				}
				
				datax += dx;
			}
		}
	}
	
}
