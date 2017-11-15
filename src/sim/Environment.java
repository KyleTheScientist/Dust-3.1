package sim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import elements.Block;
import elements.Bomb;
import elements.Cooler;
import elements.Copier;
import elements.Dirt;
import elements.Electricity;
import elements.Empty;
import elements.Eraser;
import elements.Explosion;
import elements.Fire;
import elements.Gasoline;
import elements.Grass;
import elements.Ice;
import elements.Lava;
import elements.Metal;
import elements.Radiator;
import elements.Slime;
import elements.Steam;
import elements.Stone;
import elements.Water;
import frame.Vector2D;

public class Environment {

	public static Environment instance;
	public int width, height;
	public int numElements = 0;

	public Element[][] grid;
	ArrayList<Vector2D> actives = new ArrayList<Vector2D>();

	public Environment(int width, int height) {
		Environment.instance = this;
		this.width = width;
		this.height = height;
		this.grid = new Element[width][height];
		init();
	}

	public void init() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = new Empty(new Point(i, j));
			}
		}
	}

	public void update() {
		actives.clear();
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(!grid[i][j].isStatic){
					actives.add(new Vector2D(i, j));
				}
			}
		}
		Collections.shuffle(actives);
		for (int i = 0; i < actives.size(); i++) {
			int x = actives.get(i).getX();
			int y = actives.get(i).getY();
			grid[x][y].update();
		}
	}

	public int getActiveCells() {
		int count = 0;
		ArrayList<Element> list = getGridAsList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).type != Element.EMPTY) {
				count++;
			}
		}
		return count;
	}

	public ArrayList<Element> getGridAsList() {
		ArrayList<Element> list = new ArrayList<Element>();
		for (Element[] e : grid) {
			list.addAll(Arrays.asList(e));
		}
		return list;
	}

	public void setElementAt(Point p, Element e) {
		if (inBounds(p)) {
			grid[p.x][p.y] = e;
		} else {
			System.err.println("Point out of bounds, \'setElementAt()\'");
		}
	}

	public void setElementAt(Point p, int elemType) {
		setElementAt(p, getElement(p, elemType));
	}

	public static Element getElement(Point p, int elemType) {
		switch (elemType) {
		case (Element.DIRT):
			return new Dirt(p);
		case (Element.WATER):
			return new Water(p);
		case (Element.GRASS):
			return new Grass(p);
		case (Element.STONE):
			return new Stone(p);
		case (Element.LAVA):
			return new Lava(p);
		case (Element.STEAM):
			return new Steam(p);
		case (Element.COPIER):
			return new Copier(p);
		case (Element.ERASER):
			return new Eraser(p);
		case (Element.FIRE):
			return new Fire(p);
		case (Element.ICE):
			return new Ice(p);
		case (Element.EXPLOSION):
			return new Explosion(p);
		case (Element.BOMB):
			return new Bomb(p);
		case (Element.BLOCK):
			return new Block(p);
		case (Element.METAL):
			return new Metal(p);
		case (Element.ELECTRICITY):
			return new Electricity(p);
		case (Element.GASOLINE):
			return new Gasoline(p);
		case (Element.RADIATOR):
			return new Radiator(p);
		case (Element.COOLER):
			return new Cooler(p);
		case(Element.SLIME):
			return new Slime(p);
		default:
			return new Empty(p);
		}
	}

	public Element getElementAt(Point p) {
		if (inBounds(p)) {
			return grid[p.x][p.y];
		} else {
			System.err.println("Point out of bounds");
			StackTraceElement[] s = Thread.currentThread().getStackTrace();
			System.err.println(s[2].getClassName() + "." + s[2].getMethodName() + "-line " + s[2].getLineNumber());
			return null;
		}

	}

	public static boolean inBounds(Point p) {
		if (p.x >= 0 && p.x < Environment.instance.width && p.y >= 0 && p.y < Environment.instance.height) {
			return true;
		}
		return false;
	}
}
