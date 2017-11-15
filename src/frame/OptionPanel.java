package frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import sim.Element;
import sim.Environment;

public class OptionPanel extends JPanel {

	private SpinnerModel delayModel = new SpinnerNumberModel(5, 0, 1000, 1);
	public JSpinner delaySpinner = new JSpinner(delayModel);
	private SpinnerModel brushSizeModel = new SpinnerNumberModel(5, 0, 1000, 1);
	public JSpinner brushSizeSpinner = new JSpinner(brushSizeModel);
	public JCheckBox replace = new JCheckBox("Replace");
	public static OptionPanel instance;
	static Font font = new Font("Impact", 20, 20);

	public OptionPanel(Dimension d) {
		instance = this;
		JPanel container = new JPanel();
		setDimensions(container, d);

		JPanel elementChooser = new JPanel();
		setupElementChooser(elementChooser, new Dimension(d.width, d.height - 200));
		JPanel sliderPanel = new JPanel();
		setupSliderPanel(sliderPanel, new Dimension(d.width, 100));
		JPanel buttonPanel = new JPanel();
		setupButtonPanel(buttonPanel, new Dimension(d.width, 50));

		setDimensions(replace, new Dimension(d.width, 50));
		container.add(elementChooser);
		container.add(sliderPanel);
		container.add(replace);
		container.add(buttonPanel);
		add(container);

	}

	public void setupButtonPanel(JPanel buttonPanel, Dimension d) {
		setDimensions(buttonPanel, d);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		JButton clear = new JButton("Clear");
		clear.addActionListener(e -> Environment.instance.init());
		buttonPanel.add(clear);
	}

	private void setupSliderPanel(JPanel sliderPanel, Dimension d) {
		setDimensions(sliderPanel, d);
		sliderPanel.setBackground(Color.white);
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		Dimension sDim = new Dimension(100, 30);
		setDimensions(delaySpinner, sDim);
		setDimensions(brushSizeSpinner, sDim);

		JLabel timestep = new JLabel("Time Step");
		timestep.setBackground(Color.white);
		JLabel brushSize = new JLabel("Brush Size");
		brushSize.setBackground(Color.white);
		sliderPanel.add(timestep);
		sliderPanel.add(delaySpinner);
		sliderPanel.add(brushSize);
		sliderPanel.add(brushSizeSpinner);
	}

	private void setupElementChooser(JPanel elementChooser, Dimension d) {
		elementChooser.setBackground(Color.darkGray.brighter());
		elementChooser.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setDimensions(elementChooser, d);
		int height = d.height / Element.elemCount * 2 - (d.height % Element.elemCount);
		for (int i = 0; i < Element.elemCount; i++) {
			c.gridx = i % 2;
			c.gridy = i / 2;
			c.anchor = GridBagConstraints.NORTHEAST;
			String s = "";
			Element el = Environment.getElement(null, i);
			Color color = el.getColor();
			String text = el.getClass().getSimpleName();
			ElemButton eb = new ElemButton(s, color, i, text);
			setDimensions(eb, new Dimension(d.width / 2, height));
			eb.addActionListener(e -> InputListener.selectedElement = eb.classType);
			elementChooser.add(eb, c);
		}
	}

	public void setDimensions(JComponent j, Dimension d) {
		j.setPreferredSize(d);
		j.setMinimumSize(d);
		j.setMaximumSize(d);
	}

	public static class ElemButton extends JButton {
		public static ColorThread ct;
		public Color color, textColor, borderColor;
		public int classType;
		public String text;
		int stringX, stringY;
		boolean notDetermined = true;

		public ElemButton(String s, Color color, int classType, String text) {
			super(s);
			this.color = color;
			this.classType = classType;
			this.text = text;
			setBorderPainted(false);
			if (ct == null) {
				ct = new ColorThread(5);
				ct.start();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			if (notDetermined) {
				setup(g2);
			}
			g2.setColor(color);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2.setColor(textColor);
			g2.setFont(font);
			g2.drawString(text, stringX, stringY);

			g2.setStroke(new BasicStroke(10));
			g2.setColor(borderColor);
			g2.drawRoundRect(-1, -1, getWidth() + 2, getHeight() + 2, 20, 20);

			if (InputListener.selectedElement == classType) {
				g2.setStroke(new BasicStroke(20));
				g2.setColor(ct.selectedColor);
				g2.drawRoundRect(-1, -1, getWidth() + 2, getHeight() + 2, 40, 40);
			}
		}

		public void setup(Graphics2D g2) {
			g2.setFont(font);
			FontMetrics f = g2.getFontMetrics();
			stringX = getWidth() / 2 - f.stringWidth(text) / 2;
			stringY = getHeight() / 2 + f.getAscent() / 4;

			if (colorSum(color) < 255 * 3 / 2) {
				textColor = Color.white;
				borderColor = color.brighter();
			} else {
				textColor = Color.black;
				borderColor = color.darker();
			}
			notDetermined = false;
		}

		public int colorSum(Color color) {
			return color.getRed() + color.getGreen() + color.getBlue();
		}
	}

	public static class ColorThread extends Thread {
		public long lastTick, delay = 17;
		public int colorDir = 1;
		public Color selectedColor = Color.black;
		public int sColorBrightness = 0;

		public ColorThread(int delay) {
			this.delay = delay;
		}

		public void run() {
			while (true) {
				long curTime = System.currentTimeMillis();
				if (curTime - lastTick > delay) {
					lastTick = curTime;
					sColorBrightness = (int) KMath.clamp(sColorBrightness + colorDir, 0, 255);
					if (sColorBrightness == 255) {
						colorDir *= -1;
					} else if (sColorBrightness == 0) {
						colorDir *= -1;
					}
					selectedColor = new Color(sColorBrightness, sColorBrightness, sColorBrightness);
				}
			}
		}
	}

}
