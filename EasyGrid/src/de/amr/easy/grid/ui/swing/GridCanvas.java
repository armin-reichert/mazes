package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

import javax.swing.JComponent;

import de.amr.easy.graph.api.Edge;
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

	public GridCanvas(G grid, int cellSize) {
		if (grid == null) {
			throw new IllegalArgumentException("NULL grid not allowed");
		}
		this.grid = grid;
		setDoubleBuffered(false);
		adaptSize(cellSize);
	}

	public BufferedImage getDrawingBuffer() {
		return drawingBuffer;
	}

	public G getGrid() {
		return grid;
	}

	public void setGrid(G grid) {
		if (grid == null) {
			throw new IllegalArgumentException("Canvas must have a grid");
		}
		this.grid = grid;
	}

	public void drawGridCell(int cell) {
		getRenderer().ifPresent(r -> r.drawCell(g2, grid, cell));
	}

	public void drawGridPassage(Edge edge, boolean visible) {
		getRenderer().ifPresent(r -> r.drawPassage(g2, grid, edge, visible));
	}

	public void drawGrid() {
		getRenderer().ifPresent(r -> r.drawGrid(g2, grid));
	}

	public void adaptSize(int cellSize) {
		Dimension size = new Dimension(grid.numCols() * cellSize, grid.numRows() * cellSize);
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

	public void fill(Color bgColor) {
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public Optional<GridRenderer> getRenderer() {
		return Optional.ofNullable(rendererStack.peek());
	}

	public void pushRenderer(GridRenderer renderer) {
		Optional<GridRenderer> oldRenderer = getRenderer();
		rendererStack.push(renderer);
		oldRenderer.ifPresent(old -> {
			if (old.getCellSize() != renderer.getCellSize()) {
				adaptSize(renderer.getCellSize());
			}
		});
	}

	public void popRenderer() {
		if (rendererStack.isEmpty()) {
			throw new IllegalStateException("Cannot remove last renderer");
		}
		Optional<GridRenderer> oldRenderer = getRenderer();
		GridRenderer newRenderer = rendererStack.pop();
		oldRenderer.ifPresent(old -> {
			if (old.getCellSize() != newRenderer.getCellSize()) {
				adaptSize(newRenderer.getCellSize());
			}
		});
	}
}