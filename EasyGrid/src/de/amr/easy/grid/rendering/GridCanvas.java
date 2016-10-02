package de.amr.easy.grid.rendering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JComponent;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * Canvas that listens for grid events and repaints grid accordingly.
 * 
 * @param V
 *          the vertex type
 * @param E
 *          the edge type
 * 
 * @author Armin Reichert
 */
public class GridCanvas<V, E extends Edge<V>> extends JComponent implements GraphListener<V, E> {

	private BufferedImage buffer;
	private Graphics2D g;

	private ObservableGrid2D<V, E> grid;
	private final Deque<GridRenderingModel<V>> renderStack = new LinkedList<>();
	private GridRenderer<V, E> renderer;
	private int delay;

	public GridCanvas(ObservableGrid2D<V, E> grid, GridRenderingModel<V> renderingModel) {
		setGrid(grid);
		renderStack.push(renderingModel);
		setDoubleBuffered(false);
		updateRenderingBuffer();
	}

	public void setGrid(ObservableGrid2D<V, E> grid) {
		this.grid = grid;
		grid.addGraphListener(this);
	}

	public void setRenderingModel(GridRenderingModel<V> renderingModel) {
		renderStack.clear();
		renderStack.push(renderingModel);
		updateRenderingBuffer();
	}

	private void updateRenderingBuffer() {
		int cellSize = currentRenderingModel().getCellSize();
		Dimension size = new Dimension(grid.numCols() * cellSize, grid.numRows() * cellSize);
		buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(size.width, size.height);
		g = buffer.createGraphics();
		setMinimumSize(size);
		setPreferredSize(size);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(buffer, 0, 0, null);
	}

	public void clear() {
		g.setColor(currentRenderingModel().getGridBgColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public void invalidateRenderer() {
		renderer = null;
	}

	public GridRenderingModel<V> currentRenderingModel() {
		return renderStack.peek();
	}

	public void resetRenderingModel() {
		while (renderStack.size() > 1) {
			renderStack.pop();
		}
	}

	public void pushRenderingModel(GridRenderingModel<V> renderingModel) {
		renderStack.push(renderingModel);
	}

	public void popRenderingModel() {
		renderStack.pop();
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void startListening() {
		grid.removeGraphListener(this);
		grid.addGraphListener(this);
	}

	public void stopListening() {
		grid.removeGraphListener(this);
	}

	private void sleep() {
		if (delay == 0)
			return;
		int vertexFactor = Math.max(1, (int) Math.log10(grid.vertexCount()));
		long sleepTime = (delay * (int) Math.sqrt(delay)) / vertexFactor;
		if (sleepTime == 0)
			return;
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doRender(Consumer<Graphics2D> task) {
		if (renderer == null) {
			renderer = new GridRenderer<>();
		}
		renderer.setRenderingModel(currentRenderingModel());
		task.accept(g);
		repaint();
		sleep();
	}

	public void renderGridCell(V cell) {
		doRender(g -> renderer.drawCell(g, grid, cell));
	}

	public void renderGridPassage(E edge, boolean visible) {
		doRender(g -> renderer.drawPassage(g, grid, edge, visible));
	}

	public void render() {
		doRender(g -> renderer.drawGrid(g, grid));
	}

	// GraphListener implementation

	@Override
	public void vertexChanged(V cell, Object oldValue, Object newValue) {
		renderGridCell(cell);
	}

	@Override
	public void edgeAdded(E edge) {
		renderGridPassage(edge, true);
	}

	@Override
	public void edgeRemoved(E edge) {
		renderGridPassage(edge, false);
	}

	@Override
	public void edgeChanged(E edge, Object oldValue, Object newValue) {
		renderGridPassage(edge, true);
	}

	@Override
	public void graphChanged(ObservableGraph<V, E> graph) {
		render();
	}
}