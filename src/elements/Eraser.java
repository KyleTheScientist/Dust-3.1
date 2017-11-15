package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.Display;
import frame.KMath;
import sim.Element;
import sim.Environment;

public class Eraser extends Element {
	
	public static Color tColor = Color.darkGray;
	
	Color personalColor = tColor;
	private static Color[] colors = { new Color(0, 0, 0), new Color(100, 100, 100), new Color(255, 255, 255) };

	public Eraser(Point point) {
		super(Element.ERASER, tColor, true);
		this.point = point;
	}

	@Override
	public void update() {
		super.update();
		ArrayList<Point> n = getNeighbors();
		if (n.size() > 0) {
			Point p = randomPoint(n);
			if (Environment.instance.getElementAt(p).type != ERASER) {
				Environment.instance.setElementAt(p, EMPTY);
			}
		}

		personalColor = colors[KMath.boundedRandom(0, colors.length)];
	}

	@Override
	public void draw(Graphics2D g2) {
		int s = Display.cellSize;
		g2.setColor(personalColor);
		g2.fillRect(point.x * s, point.y * s, s, s);
	}
}
