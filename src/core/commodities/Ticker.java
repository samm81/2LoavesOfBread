package core.commodities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.concurrent.LinkedBlockingQueue;

public class Ticker {
	
	LinkedBlockingQueue<Double> datum;
	
	double maxData;
	int magnitude;
	
	Color color;
	
	final int baseMaxData = 10;
	final int radius = 4;
	
	public Ticker(int magnitude, Color color) {
		this.color = color;
		this.magnitude = magnitude;
		
		datum = new LinkedBlockingQueue<Double>(magnitude);
		while(datum.offer(0d));
		
		this.maxData = baseMaxData;
		findMaxData();
	}
	
	private void findMaxData() {
		for(double data : datum) {
			if(data > maxData) {
				maxData = data;
			}
		}
	}
	
	public void addDataPoint(double dataPoint) throws InterruptedException {
		double first = datum.poll();
		datum.offer(dataPoint);
		if(first == maxData) {
			maxData = baseMaxData;
			findMaxData();
		}
		
		if(dataPoint > maxData) {
			maxData = dataPoint;
		}
		
	}
	
	public void drawSelf(int x, int y, int width, int height, Graphics2D g) {
		g.setColor(color);
		
		double dx = (double) width / (double) (magnitude - 1);
		double datax = x;
		double datay = y;
		
		for(double data : datum) {
			if(data != 0) {
				datay = y + height - ((double) height * ((double) data / maxData));
				g.fillOval((int) (datax - radius / 2d), (int) (datay - radius / 2d), radius, radius);
			}
			
			datax += dx;
		}
	}
	
	public void drawLabel(int x, int y, int height, int numLables, Graphics2D g) {
		g.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		g.setColor(this.color);
		
		double dy = (double) height / (double) numLables;
		double label = maxData;
		double dlabel = maxData / numLables;
		double labely = y;
		
		for(int i = 0; i <= numLables; i++) {
			g.drawString(String.format("%.1f", Math.abs(label)), x, (int) labely);
			labely += dy;
			label -= dlabel;
		}
		
	}
}