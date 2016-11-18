package de.amr.easy.grid.rendering.swing;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

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
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Canvas that listens for grid events and repaints grid accordingly.
 * 
 * @param V
 *          the vertex type
 * 
 * @author Armin Reichert
 */
public class SwingGridCanvas extends JComponent {

	private BufferedImage buffer;
	private Graphics2D g;

	private ObservableBareGrid2D<Integer> grid;
	private final Deque<SwingGridRenderingModel> renderStack = new LinkedList<>();
	private SwingGridRenderer renderer;
	private int delay;

	private final GraphObserver<Integer, WeightedEdge<Integer, Integer>> observer = new GraphObserver<Integer, WeightedEdge<Integer, Integer>>() {

		@Override
		public void vertexChanged(VertexChangeEvent<Integer, WeightedEdge<Integer, Integer>> event) {
			renderGridCell(event.getVertex());
		}

		@Override
		public void edgeAdded(EdgeAddedEvent<Integer, WeightedEdge<Integer, Integer>> event) {
			renderGridPassage(event.getEdge(), true);
		}

		@Override
		public void edgeRemoved(EdgeRemovedEvent<Integer, WeightedEdge<Integer, Integer>> event) {
			renderGridPassage(event.getEdge(), false);
		}

		@Override
		public void edgeChanged(EdgeChangeEvent<Integer, WeightedEdge<Integer, Integer>> event) {
			renderGridPassage(event.getEdge(), true);
		}

		@Override
		public void graphChanged(ObservableGraph<Integer, WeightedEdge<Integer, Integer>> graph) {
			render();
		}
	};

	public SwingGridCanvas(ObservableBareGrid2D<Integer> grid, SwingGridRenderingModel renderingModel) {
		setGrid(grid);
		renderStack.push(renderingModel);
		setDoubleBuffered(false);
		updateRenderingBuffer();
	}

	public void setGrid(ObservableBareGrid2D<Integer> grid) {
		this.grid = grid;
		grid.addGraphObserver(observer);
	}

	public void setRenderingModel(SwingGridRenderingModel renderingModel) {
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

	public SwingGridRenderingModel currentRenderingModel() {
		return renderStack.peek();
	}

	public void resetRenderingModel() {
		while (renderStack.size() > 1) {
			renderStack.pop();
		}
	}

	public void pushRenderingModel(SwingGridRenderingModel renderingModel) {
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
		grid.addGraphObserver(observer);
	}

	public void stopListening() {
		grid.removeGraphObserver(observer);
	}

	private void sleep() {
		if (delay > 0) {
			long sizeFactor = (long) log(grid.numCells());
			long sleepTime = delay / sizeFactor;
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void doRender(Consumer<Graphics2D> task) {
		if (renderer == null) {
			renderer = new SwingGridRenderer();
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

}