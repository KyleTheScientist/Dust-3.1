package elements;

import java.awt.Color;
import java.awt.Point;

import frame.KMath;
import sim.Element;

public class Slime extends Element {

	public static Color tColor = new Color(120, 255, 120);
	public static double tDensity = Water.tDensity + 2;

	private int bounces = 0;
	private final int MAX_BOUNCES = 10;
	private final int bounceX = 5, bounceY = 3;

	public Slime(Point p) {
		super(Element.SLIME, tColor, tDensity);
		this.point = p;
	}

	public void update() {
		super.update();
		handleGravity();
		Point b = getBelow();
		if (bounces < MAX_BOUNCES) {
			if (inBounds(b)) {
				if (!isEmpty(b) && getElementAt(b).getDensity() >= tDensity) {
					velocity.y = -velocity.y * bounceY;
					velocity.x += KMath.randomOfTwo(-bounceX, bounceX);
					bounces++;
				}
			} else {
				velocity.y = -velocity.y * bounceY;
				velocity.x += KMath.randomOfTwo(-bounceX, bounceX);
				bounces++;
			}
		}
	}

}
