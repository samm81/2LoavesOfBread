package core.commodities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.LinkedBlockingQueue;

public class Ticker {
	
	LinkedBlockingQueue<Double> datum;
	
	double maxData = 10;
	int magnitude;
	
	Color color;
	
	final int radius = 4;
	
	public Ticker(int magnitude, Color color) {
		this.color = color;
		this.magnitude = magnitude;
		
		datum = new LinkedBlockingQueue<Double>(magnitude);
		while(datum.offer(0d));
		
		//findMaxData();
	}

	/*
	private void findMaxData() {
		for(double data : datum) {
			if(data > maxData) {
				maxData = data;
			}
		}
	}
	*/
	
	public void addDataPoint(double dataPoint) throws InterruptedException {
		//double first = datum.removeFirst();
		/*
		 * if(first == maxData) {
		 * maxData = 0;
		 * findMaxData();
		 * }
		 */
		datum.poll();
		datum.offer(dataPoint);
		/*
		 * if(dataPoint > maxData) {
		 * maxData = dataPoint;
		 * }
		 */
	}
	
	public void drawSelf(int x, int y, int width, int height, Graphics2D g) {
		g.setColor(color);
		
		double dx = (double) width / (double) (magnitude - 1);
		double datax = x;
		double datay = y;
		
		System.out.println(datum.size());
		for(double data : datum) {
			if(data != 0) {
				datay = y + height - ((double) height * ((double) data / maxData));
				g.fillOval((int) (datax - radius / 2d), (int) (datay - radius / 2d), radius, radius);
			}
			
			datax += dx;
		}
	}
}