package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Lava extends Element {

	public static Color tColor = new Color(250, 140, 0);
	public static double tDensity = Water.tDensity;
	public static double tHeatCoeff = .01;

	public Lava(Point point) {
		super(Element.LAVA, tColor, tDensity);
		this.point = point;
		this.heatCoeff = .1;
		this.isLiquid = true;
		makeFreezable(.05, STONE);
	}

	public void update() {
		super.update();
		handleGravity();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = Environment.instance.getElementAt(p);
			if (e.isMeltable) {
				if (Math.random() < e.meltCoeff + heatCoeff) {
					Environment.instance.setElementAt(p, e.meltElement);
				}
			}
		}
	}
}
