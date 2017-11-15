package sim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import elements.Empty;
import frame.Display;
import frame.KMath;
import frame.Vector2D;

public abstract class Element {
	// MAKE SURE TO CHANGE ELEMCOUNT WHEN ADDING/REMOVING ELEMENT TYPES
	// AS WELL AS ADD NEW TYPE TO SWITCH-CASE IN ENVIRONMENT CLASS
	public static final int EMPTY = 0, DIRT = 1, WATER = 2, GRASS = 3, STONE = 4, LAVA = 5, STEAM = 6, COPIER = 7,
			ERASER = 8, FIRE = 9, ICE = 10, EXPLOSION = 11, BOMB = 12, BLOCK = 13, METAL = 14, ELECTRICITY = 15,
			GASOLINE = 16, RADIATOR = 17, COOLER = 18, SLIME = 19;
	public final static int elemCount = 20;
	public Point point;
	public int type;
	protected Color color;
	protected double density = 1;
	public boolean isStatic = false, isLiquid = false;
	public boolean isMeltable, isFreezeable;
	public double freezeCoeff = 0, meltCoeff = 0;
	public double heatCoeff = 0, coolCoeff = 0;
	public int meltElement = -1, freezeElement = -1;
	public Vector2D velocity = new Vector2D(0, 0);

	protected Element(int classType, Color tColor, double tDensity) {
		this.type = classType;
		this.color = colorVariation(tColor);
		this.density = tDensity;
	}

	protected Element(int classType, Color tColor, boolean isStatic) {
		this(classType, tColor, 1000);
		this.isStatic = isStatic;
	}

	public void update() {
		handleVelocity();
		if(isLiquid)
			handleLiquidSidestep();
	}

	public void handleVelocity() {
		clampVelocity();
		if (!isStatic && velocity.magnitude() > 0) {
			if (Math.abs(velocity.y) >= Math.abs(velocity.x)) {
				int sign = (velocity.y > 0) ? 1 : -1;
				Point p = new Point(point.x, point.y + sign);
				if (inBounds(p)) {
					Element e = Environment.instance.getElementAt(p);
					if (e.type == EMPTY) {
						move(p);
						velocity.y -= sign;
					} else {
						e.velocity.y += velocity.y;
						velocity.y /= 2;
					}
				} else {
					velocity.y = 0;
				}
			} else {
				int sign = (velocity.x > 0) ? 1 : -1;
				Point p = new Point(point.x + sign, point.y);
				if (inBounds(p)) {
					Element e = Environment.instance.getElementAt(p);
					if (e.type == EMPTY) {
						move(p);
						velocity.x -= sign;
					} else {
						e.velocity.addForce(0, e.velocity.x / 2);
						velocity.x /= 2;
					}
				} else {
					velocity.x = 0;
				}
			}
		}
	}

	public void handleLiquidSidestep() {
		if (inBounds(getBelow()) && !isEmpty(getBelow())) {
			ArrayList<Point> horiz = getHorizontalNeighbors();
			if (horiz.size() > 0) {
				Point p = randomPoint(horiz);
				if (isEmpty(p)) {
					move(p);
				}
			}
		}
	}

	public void destroy() {
		Environment.instance.setElementAt(point, EMPTY);
	}

	public static Element getElementAt(Point p) {
		return Environment.instance.getElementAt(p);
	}

	public static void setElementAt(Point p, Element e) {
		Environment.instance.setElementAt(p, e);
	}

	public static void setElementAt(Point p, int elemType) {
		Environment.instance.setElementAt(p, elemType);
	}

	public void clampVelocity() {
		int max = 20;
		if (velocity.getX() == 0) {
			velocity.x = 0;
		}
		if (Math.abs(velocity.getX()) > max) {
			velocity.x = (velocity.x > 0) ? max : -max;
		}
		if (velocity.getY() == 0) {
			velocity.y = 0;
		}
		if (Math.abs(velocity.getY()) > max) {
			velocity.y = (velocity.y > 0) ? max : -max;
		}
	}

	public void handleGravity() {
		boolean inBounds = inBounds(getBelow());
		if (velocity.y == 0 && inBounds) {
			velocity.y = 1;
			return;
		} else if (inBounds && velocity.y > 0) {
			if (Environment.instance.getElementAt(getBelow()).density < density) {
				swap(getBelow(), point);
			}
		}

	}

	public void draw(Graphics2D g2) {
		int s = Display.cellSize;

		g2.setColor(color);
		g2.fillRect(point.x * s, point.y * s, s, s);
	}

	public void move(Point p) {
		Environment.instance.grid[point.x][point.y] = new Empty(point);
		this.point = p;
		Environment.instance.grid[p.x][p.y] = this;
	}

	public boolean drop() {
		Point below = getBelow();
		if (inBounds(below)) {
			if (isEmpty(below)) {
				move(below);
				return true;
			} else {
				Element bElem = Environment.instance.getElementAt(below);
				if (!bElem.isStatic && bElem.density < this.density) {
					swap(below, this.point);
				}
			}
		}
		return false;
	}

	public boolean inBounds(Point p) {
		return Environment.inBounds(p);
	}

	public void makeMeltable(double meltCoeff, int meltElement) {
		this.meltCoeff = meltCoeff;
		this.meltElement = meltElement;
		this.isMeltable = true;
	}

	public void makeFreezable(double freezeCoeff, int freezeElement) {
		this.freezeCoeff = freezeCoeff;
		this.freezeElement = freezeElement;
		this.isFreezeable = true;
	}

	public void melt() {
		Environment.instance.setElementAt(point, meltElement);
	}

	public void freeze() {
		Environment.instance.setElementAt(point, freezeElement);
	}

	public void swap(Point a, Point b) {
		Environment env = Environment.instance;

		Element temp = env.getElementAt(a);
		temp.point = b;
		env.getElementAt(b).point = a;
		env.setElementAt(a, env.getElementAt(b));
		env.setElementAt(b, temp);
	}

	public ArrayList<Point> getNeighbors() {
		ArrayList<Point> n = new ArrayList<Point>();
		for (int i = -1; i < 2; i += 2) {
			if (point.x + i >= 0 && point.x + i < Environment.instance.width) {
				n.add(new Point(point.x + i, point.y));
			}
			if (point.y + i >= 0 && point.y + i < Environment.instance.height) {
				n.add(new Point(point.x, point.y + i));
			}
		}
		return n;
	}

	public ArrayList<Point> getTypeNeighbors(int type) {
		ArrayList<Point> n = getNeighbors();
		ArrayList<Point> tn = new ArrayList<Point>();
		for (int i = 0; i < n.size(); i++) {
			Point cur = n.get(i);
			if (Environment.instance.grid[cur.x][cur.y].type == type) {
				tn.add(cur);
			}
		}
		return tn;
	}

	public ArrayList<Point> getHorizontalNeighbors() {
		ArrayList<Point> emptys = getNeighbors();
		ArrayList<Point> horiz = new ArrayList<Point>();
		for (int i = 0; i < emptys.size(); i++) {
			Point h = emptys.get(i);
			if (h.y == point.y) {
				horiz.add(h);
			}
		}
		return horiz;
	}

	public ArrayList<Point> getAreaNeighbors(int area) {
		ArrayList<Point> n = new ArrayList<Point>();
		for (int i = point.x - area; i < point.x + area + 1; i++) {
			for (int j = point.y - area; j < point.y + area + 1; j++) {
				if (i == point.x && j == point.y) {
					continue;
				} else {
					if (i >= 0 && j >= 0 && i < Environment.instance.width && j < Environment.instance.height) {
						n.add(new Point(i, j));
					}
				}
			}
		}
		return n;
	}

	public ArrayList<Point> getAreaTypeNeighbors(int type, int area) {
		ArrayList<Point> n = getAreaNeighbors(area);
		ArrayList<Point> tn = new ArrayList<Point>();
		for (int i = 0; i < n.size(); i++) {
			Point cur = n.get(i);
			if (Environment.instance.grid[cur.x][cur.y].type == type) {
				tn.add(cur);
			}
		}
		return tn;
	}

	public Point randomPoint(ArrayList<Point> points) {
		int random = (int) (Math.random() * points.size());
		return points.get(random);
	}

	public Point getBelow() {
		return new Point(point.x, point.y + 1);
	}

	public Point getAbove() {
		return new Point(point.x, point.y - 1);
	}

	public boolean isEmpty(Point p) {
		if (Environment.instance.grid[p.x][p.y].type == EMPTY) {
			return true;
		}
		return false;
	}

	public Color colorVariation(Color color, int varyAmount) {
		int PoN = KMath.randomOfTwo(-1, 1);
		int variation = KMath.boundedRandom(0, varyAmount);
		int r = (int) KMath.clamp(color.getRed() + PoN * variation, 0, 255);
		int g = (int) KMath.clamp(color.getGreen() + PoN * variation, 0, 255);
		int b = (int) KMath.clamp(color.getBlue() + PoN * variation, 0, 255);
		return new Color(r, g, b);

	}

	public Color colorVariation(Color color) {
		return colorVariation(color, 10);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}
}
