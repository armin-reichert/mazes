package de.amr.easy.grid.ui.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.JComponent;

import de.amr.easy.grid.api.BareGrid2D;

/**
 * This class displays a grid in a Swing UI.
 * 
 * @author Armin Reichert
 *
 * @param <G>
 *          the Grid type
 */
public class GridCanvas<G extends BareGrid2D<?>> extends JComponent {

	private final Deque<GridRenderer> rendererStack = new ArrayDeque<>();
	private BufferedImage drawingBuffer;
	protected Graphics2D g2;
	protected G grid;

	public GridCanvas(G grid, GridRenderer renderer) {
		if (grid == null) {
			throw new IllegalArgumentException("NULL grid not allowed");
		}
		this.grid = grid;
		rendererStack.push(renderer);
		setDoubleBuffered(false);
		setBackground(renderer.getGridBgColor());
		adaptSize();
	}

	public BufferedImage getDrawingBuffer() {
		return drawingBuffer;
	}

	public G getGrid() {
		return grid;
	}

	public void setGrid(G grid) {
		if (grid == null) {
			throw new IllegalArgumentException("NULL grid not allowed");
		}
		this.grid = grid;
	}

	public int getCellSize() {
		return getRenderer().getCellSize();
	}

	public void adaptSize() {
		Dimension size = new Dimension(grid.numCols() * getCellSize(), grid.numRows() * getCellSize());
		setSize(size);
		setPreferredSize(size);
		GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		drawingBuffer = config.createCompatibleImage(size.width, size.height);
		g2 = drawingBuffer.createGraphics();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(drawingBuffer, 0, 0, null);
	}

	public void clear() {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public GridRenderer getRenderer() {
		return rendererStack.getFirst();
	}

	public void resetRenderer(GridRenderer renderer) {
		rendererStack.clear();
		rendererStack.push(renderer);
		setBackground(renderer.getGridBgColor());
		adaptSize();
	}

	public void pushRenderer(GridRenderer renderer) {
		int oldCellSize = getCellSize();
		rendererStack.push(renderer);
		setBackground(renderer.getGridBgColor());
		if (renderer.getCellSize() != oldCellSize) {
			adaptSize();
		}
	}

	public void popRenderer() {
		if (rendererStack.isEmpty()) {
			throw new IllegalStateException("Cannot pop from empty stack");
		}
		int oldCellSize = getCellSize();
		rendererStack.pop();
		setBackground(getRenderer().getGridBgColor());
		if (getCellSize() != oldCellSize) {
			adaptSize();
		}
	}
}