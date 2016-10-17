package de.amr.demos.maze.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.event.EdgeAddedEvent;
import de.amr.easy.graph.event.EdgeChangeEvent;
import de.amr.easy.graph.event.EdgeRemovedEvent;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.graph.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.rendering.swing.SwingGridRenderer;

public class GridAnimation implements GraphListener<Integer, WeightedEdge<Integer>> {

	private final ObservableDataGrid2D<TraversalState> grid;
	private final BufferedImage canvas;
	private final SwingGridRenderer renderer;
	private int delay;

	public GridAnimation(ObservableDataGrid2D<TraversalState> grid, int gridCellSize, int width, int height) {
		this.grid = grid;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		renderer = new SwingGridRenderer();
		setRenderingModel(new GridVisualization(grid, gridCellSize));
	}

	public int getDelay() {
		return delay;
	}

	public void faster(int amount) {
		setDelay(getDelay() - amount);
	}

	public void slower(int amount) {
		setDelay(getDelay() + amount);
	}

	public void setDelay(int delay) {
		this.delay = Math.max(0, delay);
	}

	public void setRenderingModel(GridVisualization renderingModel) {
		renderer.setRenderingModel(renderingModel);
	}

	public void clearCanvas() {
		Graphics2D g = getDrawGraphics();
		g.setColor(renderer.getRenderingModel().getGridBgColor());
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void render(Graphics2D g) {
		g.drawImage(canvas, 0, 0, null);
	}

	private Graphics2D getDrawGraphics() {
		return (Graphics2D) canvas.getGraphics();
	}

	private void delay() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// graph listener:

	@Override
	public void vertexChanged(VertexChangeEvent<Integer, WeightedEdge<Integer>> event) {
		delay();
		renderer.drawCell(getDrawGraphics(), grid, event.getVertex());
	}

	@Override
	public void edgeChanged(EdgeChangeEvent<Integer, WeightedEdge<Integer>> event) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, event.getEdge(), true);
	}

	@Override
	public void edgeAdded(EdgeAddedEvent<Integer, WeightedEdge<Integer>> event) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, event.getEdge(), true);
	}

	@Override
	public void edgeRemoved(EdgeRemovedEvent<Integer, WeightedEdge<Integer>> event) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, event.getEdge(), false);
	}

	@Override
	public void graphChanged(ObservableGraph<Integer, WeightedEdge<Integer>> graph) {
		delay();
		renderer.drawGrid(getDrawGraphics(), grid);
	}
}