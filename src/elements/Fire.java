package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Fire extends Element {

	public static Color tColor = new Color(255, 64, 64);
	public static double tDensity = .1;
	public static double tHeatCoeff = .001;

	public static double dissipateChance = .03;

	public Fire(Point point) {
		super(Element.FIRE, tColor, tDensity);
		this.point = point;
		this.heatCoeff = tHeatCoeff;
	}

	public void update() {
		super.update();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			for (int i = 0; i < n.size(); i++) {
				Point p = n.get(i);
				Element e = Environment.instance.getElementAt(p);
				if (e.meltElement != -1 && Math.random() < e.meltCoeff + tHeatCoeff) {
					Environment.instance.setElementAt(p, e.meltElement);
				}
			}
		}
		ArrayList<Point> empties = getTypeNeighbors(EMPTY);
		if (empties.size() > 0) {
			Point p = randomPoint(empties);
			if (p.y > point.y) {
				return;
			}
			move(p);
		}
		if (Math.random() < dissipateChance) {
			Environment.instance.setElementAt(point, EMPTY);
		}
	}

}
