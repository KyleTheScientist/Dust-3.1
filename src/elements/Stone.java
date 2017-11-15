package elements;

import java.awt.Color;
import java.awt.Point;

import sim.Element;

public class Stone extends Element {

	public static Color tColor = Color.gray;
	public static double tDensity = Dirt.tDensity;
	public static double tMeltCoeff = .001;

	public Stone(Point point) {
		super(Element.STONE, tColor, tDensity);
		this.point = point;
		makeMeltable(tMeltCoeff, LAVA);
	}

	@Override
	public void update() {
		super.update();
		handleGravity();
	}

}
