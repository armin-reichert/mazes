package de.amr.demos.grid.curves;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.impl.Top4;

/**
 * Paints a series of Hilbert curves to a canvas.
 * 
 * @author Armin Reichert
 */
public class HilbertCurveCanvasApp extends Canvas {

	private int canvasSize;
	private final Timer timer;
	private int depth;
	private int n;
	private HilbertCurve curve;
	private BufferedImage buffer;
	private int x1;
	private int y1;
	private int cellSize;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			HilbertCurveCanvasApp canvas = new HilbertCurveCanvasApp(1024);
			createFrame(canvas).setVisible(true);
			canvas.startAnimation();
		});
	}

	private static JFrame createFrame(HilbertCurveCanvasApp canvas) {
		JFrame window = new JFrame("Hilbert curves");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(canvas);
		window.pack();
		window.setSize(window.getWidth() + 20, window.getHeight() + 20);
		window.setAlwaysOnTop(true);
		return window;
	}

	public HilbertCurveCanvasApp(int canvasSize) {
		this.canvasSize = canvasSize;
		depth = 0;
		n = 1;
		setPreferredSize(new Dimension(canvasSize, canvasSize));
		setSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		timer = new Timer(1000, e -> nextCurve());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (buffer != null) {
			g.drawImage(buffer, 0, 0, null);
		}
	}

	public void startAnimation() {
		timer.start();
	}

	private void nextCurve() {
		++depth;
		n *= 2;
		cellSize = canvasSize / (2 * n);
		if (cellSize > 1) {
			curve = new HilbertCurve(depth);
			buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			drawCurve(buffer.createGraphics());
			repaint();
		} else {
			timer.stop();
		}
	}

	private void drawCurve(Graphics2D g) {
		g.translate(-cellSize, cellSize);
		x1 = getWidth() - 1;
		y1 = 0;
		for (int dir : curve) {
			int x2 = x1 + 2 * Top4.get().dx(dir) * cellSize;
			int y2 = y1 + 2 * Top4.get().dy(dir) * cellSize;
			g.setColor(Color.YELLOW);
			g.setStroke(new BasicStroke(Math.max(1, cellSize / 2)));
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		g.translate(cellSize, -cellSize);
	}
}