package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Grass extends Element {

	public static Color tColor = new Color(50, 120, 50);

	private static final double growChance = .0015;
	private static final double spreadChance = .15;

	public Grass(Point point) {
		super(Element.GRASS, tColor, true);
		this.point = point;
		makeMeltable(1, FIRE);
	}

	@Override
	public void update() {
		super.update();
		if (velocity.magnitude() > 0) {
			Environment.instance.setElementAt(point, EMPTY);
		}
		Point b = getBelow();
		Point a = getAbove();
		if (inBounds(getBelow()) && (getElementAt(b).type != GRASS && getElementAt(b).type != DIRT)) {
			Environment.instance.setElementAt(point, EMPTY);
			return;
		}
		if (inBounds(a) && !isEmpty(a)) {
			if (Environment.instance.getElementAt(getAbove()).type != GRASS) {
				Environment.instance.setElementAt(point, EMPTY);
				return;
			}
		}
		ArrayList<Point> dirtN = getAreaTypeNeighbors(DIRT, 5);
		if (dirtN.size() > 0) {
			Point p = randomPoint(dirtN);
			p.y--;
			if (inBounds(p) && isEmpty(p) && Math.random() < spreadChance) {
				Environment.instance.setElementAt(p, GRASS);
				return;
			}
		}
		if (inBounds(getAbove()) && isEmpty(getAbove())) {
			if (inBounds(getBelow()) && Math.random() < growChance)
				Environment.instance.setElementAt(getAbove(), GRASS);
		}
	}

}
