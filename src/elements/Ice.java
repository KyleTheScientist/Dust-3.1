package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Ice extends Element {

	public static Color tColor = new Color(200, 200, 255);
	public static double tDensity = Stone.tDensity;
	public static double tCoolCoeff = .03;

	public Ice(Point point) {
		super(Element.ICE, tColor, tDensity);
		makeMeltable(.5, WATER);
		this.point = point;
	}

	public void update() {
		super.update();
		handleGravity();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = Environment.instance.getElementAt(p);
			if (e.freezeElement != -1 && Math.random() < e.freezeCoeff + tCoolCoeff) {
				Environment.instance.setElementAt(p, e.freezeElement);
			}
		}

	}

}
