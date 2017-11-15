package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.BrightnessCycle;
import frame.Display;
import sim.Element;
import sim.Environment;

public class Cooler extends Element {

	public static Color tColor = new Color(0, 230, 255);
	public static double tCoolCoeff = 1;
	public static BrightnessCycle bc = new BrightnessCycle();

	public Cooler(Point point) {
		super(Element.COOLER, tColor, true);
		this.point = point;
		this.coolCoeff = tCoolCoeff;
		if (!bc.isAlive()) {
			bc.start();
		}
	}

	@Override
	public void update() {
		super.update();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = Environment.instance.getElementAt(p);
			if (e.isFreezeable) {
				if (Math.random() < tCoolCoeff + e.freezeCoeff) {
					e.freeze();
				}
			}
		}

	}

	@Override
	public void draw(Graphics2D g2) {
		Color c = bc.getColor(this.color);
		int s = Display.cellSize;
		g2.setColor(c);
		g2.fillRect(point.x * s, point.y * s, s, s);
	}

}
