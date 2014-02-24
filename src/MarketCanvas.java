import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


@SuppressWarnings("serial")
public class MarketCanvas extends DoubleBufferedCanvas {

	MarketSimulation sim;
	
	public MarketCanvas(int fps, MarketSimulation sim) {
		super(fps);
		this.sim = sim;
	}

	@Override
	void draw(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.BLACK);
		drawGraph(10, 60, this.getWidth()/2 - 10, 200, g);
		drawGraph(this.getWidth()/2 + 10, 60, this.getWidth()/2 - 30, 200, g);
		
	}
	
	private void drawGraph(int x, int y, int width, int height, Graphics2D g) {
		g.fillRoundRect(x, y, width, height, 50, 50);
	}

	@Override
	protected void updateVars() {
		// TODO Auto-generated method stub
		
	}

}
