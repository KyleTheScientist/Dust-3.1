package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Bomb extends Element {

	public static Color tColor = new Color(100, 200, 0);
	public static double tDensity = Stone.tDensity;

	public Bomb(Point point) {
		super(Element.BOMB, tColor, tDensity);
		this.point = point;
	}

	@Override
	public void update() {
		super.update();
		handleGravity();
		ArrayList<Point> n = getNeighbors();
		for (int i = 0; i < n.size(); i++) {
			Point p = n.get(i);
			Element e = Environment.instance.getElementAt(p);
			if (e.type != BOMB && !e.isStatic) {
				Environment.instance.setElementAt(point, EXPLOSION);
			}
		}
	}

}
