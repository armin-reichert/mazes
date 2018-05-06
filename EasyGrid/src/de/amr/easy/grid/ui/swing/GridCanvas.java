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

	private final Deque<GridRenderingModel> renderModelStack = new ArrayDeque<>();
	protected final GridRenderer renderer;
	private BufferedImage drawingBuffer;
	protected Graphics2D g2;
	protected G grid;

	public GridCanvas(G grid, GridRenderingModel renderModel) {
		if (grid == null) {
			throw new IllegalArgumentException("NULL grid not allowed");
		}
		if (renderModel == null) {
			throw new IllegalArgumentException("rendering model is NULL");
		}
		this.grid = grid;
		renderModelStack.push(renderModel);
		renderer = new GridRenderer(renderModel);
		setDoubleBuffered(false);
		setBackground(renderModel.getGridBgColor());
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
		return getRenderingModel().getCellSize();
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
		g2.setColor(getRenderingModel().getGridBgColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public GridRenderingModel getRenderingModel() {
		return renderModelStack.getFirst();
	}

	public void resetRenderingModel(GridRenderingModel model) {
		renderModelStack.clear();
		renderModelStack.push(model);
		setBackground(model.getGridBgColor());
		adaptSize();
	}

	public void pushRenderingModel(GridRenderingModel model) {
		renderModelStack.push(model);
		setBackground(model.getGridBgColor());
		if (model.getCellSize() != getCellSize()) {
			adaptSize();
		}
	}

	public void popRenderingModel() {
		renderModelStack.pop();
		GridRenderingModel model = renderModelStack.getFirst();
		setBackground(model.getGridBgColor());
		if (model.getCellSize() != getCellSize()) {
			adaptSize();
		}
	}
}