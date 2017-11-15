package elements;

import java.awt.Color;
import java.awt.Point;

import sim.Element;

public class Block extends Element {
	public static Color tColor = new Color(120, 120, 140);

	public Block(Point point) {
		super(Element.BLOCK, tColor, true);
		this.point = point;
	}

}
