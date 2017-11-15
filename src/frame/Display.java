package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import sim.Element;
import sim.Environment;

public class Display extends JPanel {

	public static InputListener input;
	private Environment environment;
	private Color bg = Color.darkGray.darker();
	public static int cellSize;

	public static double scalar = 1;
	public static double offset;
	public static Color selectedColor = Color.black;
	Font font = new Font("Impact", 20, 20);

	public Display(Dimension d, Environment environment) {
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		requestFocusInWindow();

		this.environment = environment;
		Display.cellSize = d.width / environment.width;
		input = new InputListener(this);
		addMouseListener(input);
		addMouseMotionListener(input);
		addMouseWheelListener(input);
		setBackground(bg);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		offset = getWidth() * (1 - scalar) / 2;
		AffineTransform af = g2.getTransform();

		g2.scale(scalar, scalar);
		g2.translate(offset, offset);
		ArrayList<Element> grid = environment.getGridAsList();
		for (int i = 0; i < grid.size(); i++) {
			Element e = grid.get(i);
			e.draw(g2);
		}

		g2.setTransform(af);
		g2.setColor(Color.white);
		g2.setFont(font);
		g2.drawString("Element Count: " + environment.getActiveCells(), 0, g2.getFontMetrics().getAscent());
		input.drawMouse(g2);
	}

}
