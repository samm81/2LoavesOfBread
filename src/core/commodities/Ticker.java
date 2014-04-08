package core.commodities;

import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Ticker {
	
	final int baseMaxData = 10;
	final int radius = 4;
	LinkedBlockingQueue<Double> datum;
	double maxData;
	int magnitude;
	Color color;
	
	public Ticker(int magnitude, Color color) {
		this.color = color;
		this.magnitude = magnitude;
		
		this.datum = new LinkedBlockingQueue<>(magnitude);
		while(this.datum.offer(1d))
			;
		
		this.maxData = baseMaxData;
		findMaxData();
	}
	
	private void findMaxData() {
		for(double data : this.datum) {
			if(data > this.maxData) {
				this.maxData = data;
			}
		}
	}
	
	public void addDataPoint(double dataPoint) throws InterruptedException {
		double first = this.datum.poll();
		this.datum.offer(dataPoint);
		if(first == this.maxData) {
			this.maxData = this.baseMaxData;
			findMaxData();
		}
		
		if(dataPoint > this.maxData) {
			this.maxData = dataPoint;
		}
		
	}
	
	public void drawSelf(int x, int y, int width, int height, Graphics2D g) {
		g.setColor(this.color);
		
		double dx = (double) width / (double) (this.magnitude - 1);
		double datax = x;
		double datay = y;
		
		for(double data : this.datum) {
			if(data != 0) {
				datay = y + height - ((double) height * (data / this.maxData));
				g.fillOval((int) (datax - this.radius / 2d), (int) (datay - this.radius / 2d), this.radius, this.radius);
			}
			
			datax += dx;
		}
	}
	
	public void drawLabel(int x, int y, int height, int numLables, Graphics2D g) {
		g.setFont(new Font("Sans Serif", Font.PLAIN, 12));
		g.setColor(this.color);
		
		double dy = (double) height / (double) numLables;
		double label = this.maxData;
		double dlabel = this.maxData / numLables;
		double labely = y;
		
		for(int i = 0; i <= numLables; i++) {
			g.drawString(String.format("%.1f", Math.abs(label)), x, (int) labely);
			labely += dy;
			label -= dlabel;
		}
		
	}
}