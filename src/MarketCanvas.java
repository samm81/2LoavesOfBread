import java.awt.Color;
import java.awt.Font;
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

		int i = 0;
		for (Commodity commodity : sim.getCommodities()) {
			int x = 10 + (this.getWidth() / 2 * (i % 2));
			int y = 60 + 210 * (i / 2);
			int width = this.getWidth() / 2 - 20;
			int height = 200;
			drawGraph(x, y, width, height, commodity, g);
			i++;
		}

	}

	private void drawGraph(int x, int y, int width, int height, Commodity commodity, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRoundRect(x, y, width, height, 50, 50);
		int offset = 3;
		g.setColor(Color.WHITE);
		//g.fillRoundRect(x + offset, y + offset, width - offset, height - offset, 50, 50); // cool effect
		g.fillRoundRect(x + offset, y + offset, width - offset * 2, height - offset * 2, 50, 50);

		int titlex = x + 30;
		int titley = y + 30;
		String name = commodity.getClass().getSimpleName();
		g.setFont(new Font("Sans Serif", Font.BOLD, 22));
		g.setColor(Color.BLACK);
		g.drawString(name, titlex, titley);
		
		for(Commodity.Graph graph : commodity.getGraphs().values()){
			graph.drawSelf(x + 30, y + 60, width - 40, height - 100, g);
		}
	}

	@Override
	protected void updateVars() {
		// TODO Auto-generated method stub

	}

}
