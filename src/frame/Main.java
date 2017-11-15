package frame;

import java.awt.Dimension;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import sim.Environment;

public class Main extends JFrame implements Runnable {

	Dimension dDim = new Dimension(800, 800);
	Dimension oDim = new Dimension(200, dDim.height);
	Dimension fDim = new Dimension(oDim.width + dDim.width, dDim.height);

	Display display;
	OptionPanel optionPanel;
	Environment environment;
	
	Random r;

	private int delay = 0;
	private final int GRIDW = 100, GRIDH = 100;

	public Main() {
		setResizable(false);
		setSize(fDim);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		environment = new Environment(GRIDW, GRIDH);
		optionPanel = new OptionPanel(oDim);

		add(optionPanel);
		display = new Display(dDim, environment);
		add(display);
		pack();
	}

	public void run() {
		while (true) {
			InputListener.update();
			environment.update();
			repaint();
			delay = (Integer) optionPanel.delaySpinner.getValue();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(String[] args) {
		new Thread(new Main()).start();
	}

}
