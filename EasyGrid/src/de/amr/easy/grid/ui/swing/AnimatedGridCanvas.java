package de.amr.easy.grid.ui.swing;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * This class displays an observable grid and reacts on grid change events. It can be used for
 * displaying animations of grid algorithms like maze generation.
 * 
 * @author Armin Reichert
 */
public class AnimatedGridCanvas extends GridCanvas<ObservableGrid2D<TraversalState, Integer>>
		implements GraphObserver<WeightedEdge<Integer>> {

	private int delayMillis;

	public AnimatedGridCanvas(ObservableGrid2D<TraversalState, Integer> grid, GridRenderer renderer) {
		super(grid, renderer);
		grid.addGraphObserver(this);
	}

	@Override
	public void setGrid(ObservableGrid2D<TraversalState, Integer> grid) {
		if (grid == null) {
			throw new IllegalArgumentException("NULL grid not allowed");
		}
		if (this.grid != null) {
			this.grid.removeGraphObserver(this);
		}
		this.grid = grid;
		this.grid.addGraphObserver(this);
	}

	private void drawDelayed(Runnable code) {
		if (delayMillis > 0) {
			try {
				Thread.sleep(delayMillis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		code.run();
		repaint();
	}

	public int getDelay() {
		return delayMillis;
	}

	public void setDelay(int millis) {
		this.delayMillis = millis;
	}

	public void startListening() {
		grid.addGraphObserver(this);
	}

	public void stopListening() {
		grid.removeGraphObserver(this);
	}

	public void drawGridCell(int cell) {
		drawDelayed(() -> getRenderer().drawCell(g2, grid, cell));
	}

	public void drawGridPassage(Edge edge, boolean visible) {
		drawDelayed(() -> getRenderer().drawPassage(g2, grid, edge, visible));
	}

	public void drawGrid() {
		drawDelayed(() -> getRenderer().drawGrid(g2, grid));
	}

	// implement GraphObserver interface

	@Override
	public void vertexChanged(VertexChangeEvent event) {
		drawGridCell(event.getVertex());
	}

	@Override
	public void edgeAdded(EdgeAddedEvent<WeightedEdge<Integer>> event) {
		drawGridPassage(event.getEdge(), true);
	}

	@Override
	public void edgeRemoved(EdgeRemovedEvent<WeightedEdge<Integer>> event) {
		drawGridPassage(event.getEdge(), false);
	}

	@Override
	public void edgeChanged(EdgeChangeEvent<WeightedEdge<Integer>> event) {
		drawGridPassage(event.getEdge(), true);
	}

	@Override
	public void graphChanged(ObservableGraph<WeightedEdge<Integer>> graph) {
		drawGrid();
	}
}