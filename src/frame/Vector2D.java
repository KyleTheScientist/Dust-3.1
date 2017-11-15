package frame;

import java.awt.Point;

public class Vector2D {

	public double x, y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D perpendicular() {
		return new Vector2D(y, -x);
	}

	public static Vector2D getVector(Point a, Point b) {
		return new Vector2D(b.x - a.x, -(b.y - a.y));
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y));
	}

	public Vector2D getNormal() {
		return new Vector2D(x / magnitude(), y / magnitude());
	}

	public double dot(Vector2D v2) {
		return x * v2.x + y * v2.y;
	}

	public Vector2D addForce(Vector2D v) {
		return addForce(v.x, v.y);
	}

	public Vector2D scale(double scalar) {
		return new Vector2D(this.x * scalar, this.y * scalar);
	}

	public Vector2D addForce(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public void rotate(double n) {
		n = Math.toRadians(n);
		double rx = (this.x * Math.cos(n)) - (this.y * Math.sin(n));
		double ry = (this.x * Math.sin(n)) + (this.y * Math.cos(n));
		x = rx;
		y = ry;
	}

	@Override
	public String toString() {
		String s = "[ " + x + ", " + y + " ]";
		return s;
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

}
