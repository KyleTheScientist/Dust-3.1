package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.Display;
import sim.Element;
import sim.Environment;

public class Water extends Element {

	private static Color tColor = new Color(100, 200, 255);
	public static double tDensity = 5;
	public static double makeStoneCoeff = .5;

	public Water(Point point) {
		super(Element.WATER, tColor, tDensity);
		this.point = point;
		this.isLiquid = true;
		makeMeltable(.1, STEAM);
		makeFreezable(.1, ICE);
	}

	public void update() {
		super.update();
		handleGravity();
		ArrayList<Point> n = getTypeNeighbors(LAVA);
		if (n.size() > 0) {
			Point p = randomPoint(n);
			Element e = Environment.instance.getElementAt(p);
			if (Math.random() < makeStoneCoeff) {
				Environment.instance.setElementAt(p, e.freezeElement);
			}
		}
	}

}
