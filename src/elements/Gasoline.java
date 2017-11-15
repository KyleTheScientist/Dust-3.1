package elements;

import java.awt.Color;
import java.awt.Point;

import sim.Element;

public class Gasoline extends Element {

	public static Color tColor = new Color(170, 180, 0);
	public static double tDensity = Water.tDensity-1;

	public Gasoline(Point point) {
		super(Element.GASOLINE, tColor, tDensity);
		this.point = point;
		this.isLiquid = true;
		makeMeltable(.2, FIRE);
	}

	@Override
	public void update() {
		super.update();
		handleGravity();
	}

}
