package frame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.nio.Buffer;

import javax.swing.JComponent;

import sim.Element;
import sim.Environment;

public class InputListener extends MouseAdapter {

	JComponent component;
	private static int brushSize = 1;
	private static boolean mouseDown = false;
	private static Point mouseLocation = new Point(0, 0);
	public static int selectedElement = 0;

	public static void update() {
		brushSize = (int) OptionPanel.instance.brushSizeSpinner.getValue();
		if (mouseDown) {
			for (int x = -brushSize; x <= brushSize; x++) {
				for (int y = -brushSize; y <= brushSize; y++) {
					int px = (mouseLocation.x / Display.cellSize) + x;
					int py = (mouseLocation.y / Display.cellSize) + y;
					Point p = new Point(px, py);

					if (OptionPanel.instance.replace.isSelected() && Environment.inBounds(p)) {
						Environment.instance.setElementAt(p, selectedElement);
					} else {
						if (Environment.inBounds(p) && Environment.instance.getElementAt(p).type == Element.EMPTY) {
							Environment.instance.setElementAt(p, selectedElement);
						}
					}
				}
			}
		}
	}

	public InputListener(JComponent component) {
		this.component = component;
	}

	public void mousePressed(MouseEvent e) {
		mouseDown = true;
		mouseLocation = e.getPoint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		if (e.getWheelRotation() < 0) {
			if (brushSize < 100) {
				brushSize++;
			}
		} else {
			if (brushSize > 0) {
				brushSize--;
			}
		}
		OptionPanel.instance.brushSizeSpinner.setValue(brushSize);
	}

	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
		mouseLocation = e.getPoint();
	}

	public void mouseDragged(MouseEvent e) {
		mouseDown = true;
		mouseLocation = e.getPoint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLocation = e.getPoint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseLocation.x = -1000;
		mouseLocation.y = -1000;
	}

	public void drawMouse(Graphics2D g2) {
		int s = Display.cellSize;
		int radius = brushSize * s;

		g2.drawRect(mouseLocation.x - radius, mouseLocation.y - radius, radius * 2, radius * 2);
	}

}
