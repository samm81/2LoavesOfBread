import java.awt.Color;
import java.awt.Graphics;


@SuppressWarnings("serial")
public class Wheee extends DoubleBufferedCanvas {

	public Wheee(int fps) {
		super(fps);
	}

	public static void main(String[] args) {
		Wheee wheee = new Wheee(30);
		@SuppressWarnings("unused")
		Framer fr = new Framer("Whoo", 100, 100, 500, 500, wheee);
	}

	int x;
	int y;
	int xShadow;
	int yShadow;
	int width = 40;
	int height = 40;
	int length = 100;
	double shadowLength = 1.06;
	
	double speed = .1;
	double frame = 0;
	
	@Override
	void draw(Graphics g) {
		g.setColor(Color.red.darker().darker());
		g.fillOval(xShadow, yShadow, width, height);
		g.setColor(Color.RED);
		g.fillOval(x, y, width, height);
 	}

	@Override
	protected void updateVars() {
		frame += speed;
		x = (int) (this.getWidth() / 2.0 + length * Math.cos(frame));
		y = (int) (this.getHeight() / 2.0 - length * Math.sin(frame));
		xShadow = (int) (this.getWidth() / 2.0 + length * shadowLength * Math.cos(frame));
		yShadow = (int) (this.getHeight() / 2.0 - length * shadowLength * Math.sin(frame));
		
		x -= width/2;
		y -= height/2;
		xShadow -= width/2;
		yShadow -= height/2;
	}
}
