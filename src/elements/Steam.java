package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Steam extends Element {

	public static Color tColor = new Color(240, 240, 240);
	public static double tDensity = 0;

	public static double dissipateChance = .001;
	public static double lightningChance = .0005;

	public Steam(Point point) {
		super(Element.STEAM, tColor, tDensity);
		this.point = point;
		makeFreezable(.1, WATER);
	}

	public void update() {
		super.update();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = getElementAt(p);
			if (e.type == STEAM && Math.random() < lightningChance) {
				setElementAt(p, ELECTRICITY);
				return;
			} else if (e.type == EMPTY) {
				if (p.y > point.y) {
					return;
				}
				move(p);
			}
		}
		if (Math.random() < dissipateChance) {
			destroy();
		}
	}

}
