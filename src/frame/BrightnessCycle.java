package frame;

import java.awt.Color;

public class BrightnessCycle extends Thread {

	private int min = -20, max = 20;
	private int delay = 20;
	private int dir = 1;
	public int bVal = 0;

	public void run() {
		while (true) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bVal += dir;
			if (bVal > max || bVal < min) {
				dir *= -1;
			}
		}
	}

	public Color getColor(Color color) {
		int r = (int) KMath.clamp(color.getRed() + bVal, 0, 255);
		int g = (int) KMath.clamp(color.getGreen() + bVal, 0, 255);
		int b = (int) KMath.clamp(color.getBlue() + bVal, 0, 255);
		return new Color(r, g, b);
	}

}
