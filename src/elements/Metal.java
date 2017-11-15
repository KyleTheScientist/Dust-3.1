package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import sim.Element;
import sim.Environment;

public class Metal extends Element {

	public static Color tColor = new Color(150, 150, 150);
	public static Color chargedColor = new Color(230, 230, 0);
	public static double meltCoeff = .001;
	public boolean isCharged;

	public Metal(Point point) {
		super(Element.METAL, tColor, true);
		this.point = point;
		makeMeltable(meltCoeff, LAVA);
	}

	@Override
	public void update() {
		super.update();
		if (isCharged) {
			setColor(chargedColor);
			ArrayList<Point> n = getTypeNeighbors(METAL);
			if (n.size() >= 2) {
				Metal m = (Metal) Environment.instance.getElementAt(randomPoint(n));
				if (!m.isCharged) {
					m.isCharged = true;
					this.isCharged = false;
				}
			} else {
				ArrayList<Point> e = getTypeNeighbors(EMPTY);
				if (e.size() > 0) {
					Environment.instance.setElementAt(randomPoint(e), ELECTRICITY);
					isCharged = false;
				}
			}
		} else {
			setColor(tColor);
		}
	}
}
