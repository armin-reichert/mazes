package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.swing.JComponent;

import de.amr.easy.data.Stack;
import de.amr.easy.grid.impl.GridGraph;

/**
 * A Swing component for displaying a grid.
 * 
 * @author Armin Reichert
 */
public class GridCanvas extends JComponent {

	protected final Stack<GridRenderer> rendererStack = new Stack<>();
	protected GridGraph<?, ?> grid;
	protected BufferedImage buffer;
	protected Graphics2D g2;

	public GridCanvas(GridGraph<?, ?> grid, int cellSize) {
		if (grid == null) {
			throw new IllegalArgumentException("No grid specified");
		}
		this.grid = grid;
		setDoubleBuffered(false);
		resizeTo(cellSize);
	}

	public GridGraph<?, ?> getGrid() {
		return grid;
	}

	public void setGrid(GridGraph<?, ?> grid) {
		if (grid == null) {
			throw new IllegalArgumentException("No grid specified");
		}
		this.grid = grid;
		drawGrid();
	}

	public BufferedImage getDrawingBuffer() {
		return buffer;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(buffer, 0, 0, null);
	}

	public void clear() {
		getRenderer().ifPresent(r -> fill(r.getModel().getGridBgColor()));
		repaint();
	}

	public void fill(Color bgColor) {
		g2.setColor(bgColor);
		g2.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public void drawGridCell(int cell) {
		getRenderer().ifPresent(r -> r.drawCell(g2, grid, cell));
		repaint();
	}

	public void drawGridPassage(int either, int other, boolean visible) {
		getRenderer().ifPresent(r -> r.drawPassage(g2, grid, either, other, visible));
		repaint();
	}

	public void drawGrid() {
		getRenderer().ifPresent(r -> r.drawGrid(g2, grid));
		repaint();
	}

	protected void resizeTo(int cellSize) {
		int width = grid.numCols() * cellSize, height = grid.numRows() * cellSize;
		setSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height);
		g2 = buffer.createGraphics();
	}

	public void setCellSize(int cellSize) {
		resizeTo(cellSize);
		repaint();
	}

	public Optional<GridRenderer> getRenderer() {
		return rendererStack.peek();
	}

	public void pushRenderer(GridRenderer newRenderer) {
		getRenderer().ifPresent(oldRenderer -> {
			if (oldRenderer.getModel().getCellSize() != newRenderer.getModel().getCellSize()) {
				resizeTo(newRenderer.getModel().getCellSize());
			}
		});
		rendererStack.push(newRenderer);
		repaint();
	}

	public GridRenderer popRenderer() {
		if (rendererStack.isEmpty()) {
			throw new IllegalStateException("Cannot remove last renderer");
		}
		GridRenderer oldRenderer = rendererStack.pop();
		getRenderer().ifPresent(newRenderer -> {
			if (oldRenderer.getModel().getCellSize() != newRenderer.getModel().getCellSize()) {
				resizeTo(newRenderer.getModel().getCellSize());
			}
		});
		repaint();
		return oldRenderer;
	}
}