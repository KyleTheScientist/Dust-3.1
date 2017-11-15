package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Electricity extends Element {

	public static Color tColor = new Color(255, 255, 0);
	public static double tDensity = 0;
	public static double tHeatCoeff = .4;
	public static double dissipateChance = .05;

	public Electricity(Point point) {
		super(Element.ELECTRICITY, tColor, tDensity);
		this.point = point;
		this.heatCoeff = tHeatCoeff;
	}

	public void update() {
		super.update();

		if (Math.random() < dissipateChance) {
			destroy();
		}
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = Environment.instance.getElementAt(p);
			if (e.type == METAL) {
				Metal m = (Metal) e;
				m.isCharged = true;
				destroy();
			} else if (e.isMeltable && Math.random() < e.meltCoeff) {
				Environment.instance.setElementAt(p, e.meltElement);
				Environment.instance.setElementAt(point, FIRE);
			} else if (e.type == EMPTY) {
				if (p.y < point.y) {
					return;
				}
				move(p);
			} else if (!e.isStatic && e.type != ELECTRICITY && e.type != STEAM) {
				Environment.instance.setElementAt(point, EXPLOSION);
			}
		}
	}
}
