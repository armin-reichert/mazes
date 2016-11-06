package de.amr.demos.grid.curves;

import static java.lang.Math.pow;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.Timer;

import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.curves.HilbertCurve;

public class HilbertCurveCanvasApp extends Canvas {

	private int size;
	private final Timer timer;

	private int depth;
	private HilbertCurve curve;
	private int x;
	private int y;
	private double lineLen;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			HilbertCurveCanvasApp canvas = new HilbertCurveCanvasApp(900);
			JFrame window = new JFrame("Hilbert curves");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.add(canvas);
			window.pack();
			window.setSize(window.getWidth() + 20, window.getHeight() + 20);
			window.setVisible(true);
			canvas.startAnimation();
		});
	}

	public HilbertCurveCanvasApp(int size) {
		this.size = size;
		depth = 0;
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(size, size));
		setSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		timer = new Timer(1500, e -> nextCurve());
	}

	public void startAnimation() {
		timer.start();
	}

	private void nextCurve() {
		++depth;
		double newLineLen = size / (pow(2, depth) - 1);
		if (newLineLen >= 2.0) {
			curve = new HilbertCurve(depth);
			lineLen = newLineLen;
			repaint();
		} else {
			timer.stop();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (curve != null) {
			g.translate(-10, 10);
			System.out.println("paint: Line length: " + lineLen);
			x = getWidth() - 1;
			y = 0;
			for (Dir4 dir : curve) {
				int newX = x + dir.dx * (int) lineLen;
				int newY = y + dir.dy * (int) lineLen;
				g.setColor(Color.BLUE);
				g.drawLine(x, y, newX, newY);
				x = newX;
				y = newY;
			}
			g.translate(10, -10);
		}
	}
}
