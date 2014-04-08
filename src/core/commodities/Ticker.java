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
        while (this.datum.offer(1d)) ;

        this.maxData = this.baseMaxData;
        findMaxData();
    }

    private void findMaxData() {
        for (double data : this.datum) {
            if (data > this.maxData) {
                this.maxData = data;
            }
        }
    }

    public void addDataPoint(double dataPoint) throws InterruptedException {
        double first = this.datum.poll();
        this.datum.offer(dataPoint);
        if (first == this.maxData) {
            this.maxData = this.baseMaxData;
            findMaxData();
        }

        if (dataPoint > this.maxData) {
            this.maxData = dataPoint;
        }

    }

    public void drawSelf(int x, int y, int width, int height, Graphics2D g) {
        g.setColor(this.color);

        double dx = (double) width / (double) (this.magnitude - 1);
        double dataX = x;

        for (double data : this.datum) {
            if (data != 0) {
                double dataY = y + height - ((double) height * ( data / this.maxData));
                g.fillOval((int) (dataX - this.radius / 2d), (int) (dataY - this.radius / 2d), this.radius, this.radius);
            }

            dataX += dx;
        }
    }

    public void drawLabel(int x, int y, int height, int numLabels, Graphics2D g) {
        g.setFont(new Font("Sans Serif", Font.PLAIN, 12));
        g.setColor(this.color);

        double dy = (double) height / (double) numLabels;
        double label = this.maxData;
        double dLabel = this.maxData / numLabels;
        double labelY = y;

        for (int i = 0; i <= numLabels; i++) {
            g.drawString(String.format("%.1f", Math.abs(label)), x, (int) labelY);
            labelY += dy;
            label -= dLabel;
        }

    }
}