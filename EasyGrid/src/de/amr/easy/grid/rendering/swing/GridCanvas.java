package de.amr.easy.grid.rendering.swing;

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
import de.amr.easy.graph.event.EdgeAddedEvent;
import de.amr.easy.graph.event.EdgeChangeEvent;
import de.amr.easy.graph.event.EdgeRemovedEvent;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.graph.event.VertexChangeEvent;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * Canvas that listens for grid events and repaints grid accordingly.
 * 
 * @param V
 *          the vertex type
 * 
 * @author Armin Reichert
 */
public class GridCanvas extends JComponent implements GraphListener<Integer, DefaultEdge<Integer>> {

	private BufferedImage buffer;
	private Graphics2D g;

	private ObservableGrid2D grid;
	private final Deque<GridRenderingModel> renderStack = new LinkedList<>();
	private GridRenderer renderer;
	private int delay;

	public GridCanvas(ObservableGrid2D grid, GridRenderingModel renderingModel) {
		setGrid(grid);
		renderStack.push(renderingModel);
		setDoubleBuffered(false);
		updateRenderingBuffer();
	}

	public void setGrid(ObservableGrid2D grid) {
		this.grid = grid;
		grid.addGraphListener(this);
	}

	public void setRenderingModel(GridRenderingModel renderingModel) {
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

	public GridRenderingModel currentRenderingModel() {
		return renderStack.peek();
	}

	public void resetRenderingModel() {
		while (renderStack.size() > 1) {
			renderStack.pop();
		}
	}

	public void pushRenderingModel(GridRenderingModel renderingModel) {
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
			renderer = new GridRenderer();
		}
		renderer.setRenderingModel(currentRenderingModel());
		task.accept(g);
		repaint();
		sleep();
	}

	public void renderGridCell(Integer cell) {
		doRender(g -> renderer.drawCell(g, grid, cell));
	}

	public void renderGridPassage(Edge<Integer> edge, boolean visible) {
		doRender(g -> renderer.drawPassage(g, grid, edge, visible));
	}

	public void render() {
		doRender(g -> renderer.drawGrid(g, grid));
	}

	// GraphListener implementation

	@Override
	public void vertexChanged(VertexChangeEvent<Integer, DefaultEdge<Integer>> event) {
		renderGridCell(event.getVertex());
	}

	@Override
	public void edgeAdded(EdgeAddedEvent<Integer, DefaultEdge<Integer>> event) {
		renderGridPassage(event.getEdge(), true);
	}

	@Override
	public void edgeRemoved(EdgeRemovedEvent<Integer, DefaultEdge<Integer>> event) {
		renderGridPassage(event.getEdge(), false);
	}

	@Override
	public void edgeChanged(EdgeChangeEvent<Integer, DefaultEdge<Integer>> event) {
		renderGridPassage(event.getEdge(), true);
	}

	@Override
	public void graphChanged(ObservableGraph<Integer, DefaultEdge<Integer>> graph) {
		render();
	}
}