package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.BrightnessCycle;
import frame.Display;
import sim.Element;
import sim.Environment;

public class Radiator extends Element {

	public static Color tColor = new Color(255, 230, 0);
	public static double tHeatCoeff = 1;
	public static BrightnessCycle bc = new BrightnessCycle();

	public Radiator(Point point) {
		super(Element.RADIATOR, tColor, true);
		this.point = point;
		this.heatCoeff = tHeatCoeff;
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
			if (e.isMeltable) {
				if (Math.random() < tHeatCoeff + e.meltCoeff) {
					e.melt();
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
