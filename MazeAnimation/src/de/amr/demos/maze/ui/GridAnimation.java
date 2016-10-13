package de.amr.demos.maze.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.rendering.GridRenderer;

public class GridAnimation implements GraphListener<Integer, DefaultEdge<Integer>> {

	private final ObservableDataGrid2D<Integer, TraversalState> grid;
	private final BufferedImage canvas;
	private final GridRenderer<Integer> renderer;
	private int delay;

	public GridAnimation(ObservableDataGrid2D<Integer, TraversalState> grid, int gridCellSize, int width, int height) {
		this.grid = grid;
		canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		renderer = new GridRenderer<>();
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
	public void vertexChanged(Integer cell, Object oldValue, Object newValue) {
		delay();
		renderer.drawCell(getDrawGraphics(), grid, cell);
	}

	@Override
	public void edgeChanged(DefaultEdge<Integer> edge, Object oldValue, Object newValue) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, edge, true);
	}

	@Override
	public void edgeAdded(DefaultEdge<Integer> edge) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, edge, true);
	}

	@Override
	public void edgeRemoved(DefaultEdge<Integer> edge) {
		delay();
		renderer.drawPassage(getDrawGraphics(), grid, edge, false);
	}

	@Override
	public void graphChanged(ObservableGraph<Integer, DefaultEdge<Integer>> graph) {
		delay();
		renderer.drawGrid(getDrawGraphics(), grid);
	}
}