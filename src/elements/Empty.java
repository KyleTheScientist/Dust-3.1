package elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import frame.Display;
import sim.Element;

public class Empty extends Element {

	public Empty(Point point) {
		super(Element.EMPTY, Color.darkGray, true);
		this.isStatic = true;
		this.point = point;
	}

	public void update() {
	}

	@Override
	public void draw(Graphics2D g2) {
		int s = Display.cellSize;
		//g2.setColor(Color.white.darker());
		//g2.drawRect(point.x * s, point.y * s, s, s);
	}

}
