package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import frame.Display;
import frame.KMath;
import sim.Element;
import sim.Environment;

public class Copier extends Element {

	public static Color tColor = new Color(255, 255, 0);
	
	public int copyType = -1;
	public Color personalColor = tColor;

	public Copier(Point point) {
		super(COPIER, tColor, true);
		this.point = point;
	}

	public void update() {
		super.update();
		if (copyType == -1) {
			personalColor = KMath.randomColor();
			ArrayList<Point> n = getNeighbors();
			if (n.size() > 0) {
				Point p = randomPoint(n);
				int pType = Environment.instance.getElementAt(p).type;
				if (pType != EMPTY && pType != COPIER) {
					this.copyType = pType;
				}
			}
		} else {
			personalColor = colorVariation(Environment.getElement(null, copyType).getColor(), 20).darker().darker();
			ArrayList<Point> n = getAreaNeighbors(1);
			Point p = randomPoint(n);
			if (isEmpty(p)) {
				Environment.instance.setElementAt(p, copyType);
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		int s = Display.cellSize;
		g2.setColor(personalColor);
		g2.fillRect(point.x * s, point.y * s, s, s);
	}

}
