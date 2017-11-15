package elements;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import frame.KMath;
import frame.Vector2D;
import sim.Element;
import sim.Environment;

public class Explosion extends Element {

	public static Color tColor = new Color(255, 85, 45);

	public final int range = 0;

	public Explosion(Point point) {
		super(Element.EXPLOSION, tColor, true);
		this.point = point;
	}

	@Override
	public void update() {
		super.update();
		ArrayList<Point> n = getAreaNeighbors(5);
		for (int i = 0; i < n.size(); i++) {
			Point p = n.get(i);
			Element e = Environment.instance.getElementAt(p);
			if (e.type != EMPTY && e.isStatic == false) {
				double dx = point.x - p.x;
				double dy = point.y - p.y;
				if (Math.abs(dx) < .1)
					dx += KMath.randomOfTwo(-1, 1);
				if (Math.abs(dy) < .1)
					dy += KMath.randomOfTwo(-1, 1);

				e.velocity.addForce(new Vector2D(dx * -20, dy * -20));
			}
		}
		Environment.instance.setElementAt(point, EMPTY);
	}

}
