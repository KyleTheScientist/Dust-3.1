package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.Display;
import sim.Element;
import sim.Environment;

public class Dirt extends Element {

	private static Color tColor = new Color(150, 100, 50);
	public static double tDensity = 10;

	public Dirt(Point point) {
		super(Element.DIRT, tColor, tDensity);
		this.point = point;
	}

	public void update() {
		super.update();
		handleGravity();
		ArrayList<Point> horiz = getHorizontalNeighbors();
		if (horiz.size() > 0) {
			Point p = randomPoint(horiz);
			if (Environment.instance.getElementAt(p).type == WATER && inBounds(getAbove()) && isEmpty(getAbove())) {
				if (Math.random() < .1) {
					Environment.instance.setElementAt(getAbove(), GRASS);
				}
			}
		}
	}

}
