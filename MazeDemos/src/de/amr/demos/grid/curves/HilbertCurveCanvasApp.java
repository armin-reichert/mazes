package de.amr.demos.grid.curves;

import static de.amr.easy.grid.impl.Top4.Top4;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

import de.amr.easy.grid.curves.HilbertCurve;

public class HilbertCurveCanvasApp extends Canvas {

	private int canvasSize;
	private final Timer timer;
	private int depth;
	private int n;
	private HilbertCurve curve;
	private BufferedImage curveDrawing;
	private int x;
	private int y;
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
		if (curveDrawing != null) {
			g.drawImage(curveDrawing, 0, 0, null);
		}
	}

	public void startAnimation() {
		timer.start();
	}

	private void nextCurve() {
		++depth;
		n *= 2;
		cellSize = canvasSize / (2 * n);
		if (cellSize > 4) {
			curve = new HilbertCurve(depth);
			curveDrawing = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			drawCurve(curveDrawing.getGraphics());
			repaint();
		} else {
			timer.stop();
			System.out.println("Stopped at cell size " + cellSize);
		}
	}

	private void drawCurve(Graphics g) {
		System.out.println("drawCurve: Cell size: " + cellSize);
		g.translate(-cellSize, cellSize);
		x = getWidth() - 1;
		y = 0;
		for (int dir : curve) {
			int newX = x + 2 * Top4.dx(dir) * cellSize;
			int newY = y + 2 * Top4.dy(dir) * cellSize;
			g.setColor(Color.BLUE);
			g.drawLine(x, y, newX, newY);
			x = newX;
			y = newY;
		}
		g.translate(cellSize, -cellSize);
	}
}
