package core.GUI;

import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Ticker {

    final int baseMaxData = 10;
    final int radius = 4;
    LinkedBlockingQueue<Double> datum;
    double maxData;
    int magnitude;
    int numSteps;
    Color color;

    public Ticker(int magnitude, int numSteps, Color color) {
        this.color = color;
        this.numSteps = numSteps;
        this.magnitude = magnitude;

        this.datum = new LinkedBlockingQueue<>(magnitude);
        while(this.datum.offer(1d))
            ;

        this.maxData = this.baseMaxData;
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
        g.setColor(new Color(.3f, .3f, .3f));
        double dy = (double) height / (double) numSteps;
        for(int i = 0; i <= numSteps; i++) {
            int x1 = x;
            int y1 = y + (int) (i * dy);
            int x2 = x + width;
            int y2 = y1;
            g.drawLine(x1, y1, x2, y2);
        }

        g.setColor(this.color);

        double dx = (double) width / (double) (this.magnitude - 1);
        double dataX = x;

        for(double data : this.datum) {
            if(data != 0) {
                double dataY = y + height - ((double) height * (data / this.maxData));
                g.fillOval((int) (dataX - this.radius / 2d), (int) (dataY - this.radius / 2d), this.radius, this.radius);
            }

            dataX += dx;
        }
    }

    public void drawLabel(int x, int y, int height, Graphics2D g) {
        g.setFont(new Font("Sans Serif", Font.PLAIN, 12));
        g.setColor(this.color);

        double dy = (double) height / (double) numSteps;
        double label = this.maxData;
        double dLabel = this.maxData / numSteps;
        double labelY = y;

        for(int i = 0; i <= numSteps; i++) {
            g.drawString(String.format("%.1f", Math.abs(label)), x, (int) labelY);
            labelY += dy;
            label -= dLabel;
        }

    }
}