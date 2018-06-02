package de.amr.easy.grid.ui.experimental;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.ConfigurableGridRenderer;

/**
 * Canvas that can display a grid, a colored distance map and a path between two cells.
 * 
 * @author Armin Reichert
 */
public class LayeredGridCanvas extends LayeredCanvas implements GraphObserver<WeightedEdge<Integer>> {

	private enum Layers {
		Grid, Distances, Path
	};

	protected int cellSize;
	protected boolean pathDisplayed;
	protected boolean distancesDisplayed;
	protected ObservableGrid2D<TraversalState, Integer> grid;
	protected BreadthFirstTraversal bfs;
	protected Iterable<Integer> path;

	public LayeredGridCanvas(int width, int height, int cellSize) {
		super(width, height);
		this.cellSize = cellSize;
		pathDisplayed = true;
		distancesDisplayed = true;
		setBackground(Color.BLACK);
		newGrid();
		addGridLayer();
		addDistanceLayer();
		addPathLayer();
	}

	protected void newGrid() {
		Dimension size = getSize();
		int rows = size.height / cellSize, cols = size.width / cellSize;
		if (grid != null) {
			grid.removeGraphObserver(this);
		}
		grid = new ObservableGrid<>(cols, rows, Top4.get(), TraversalState.UNVISITED, false);
		grid.addGraphObserver(this);
	}

	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		if (this.cellSize != cellSize) {
			this.cellSize = cellSize;
			newGrid();
			path = null;
			bfs = null;
			layers.clear();
			addGridLayer();
			addDistanceLayer();
			addPathLayer();
			clear();
			repaint();
		}
	}

	public boolean isPathDisplayed() {
		return pathDisplayed;
	}

	public void setPathDisplayed(boolean pathDisplayed) {
		this.pathDisplayed = pathDisplayed;
	}

	public boolean isDistancesDisplayed() {
		return distancesDisplayed;
	}

	public void setDistancesDisplayed(boolean distancesDisplayed) {
		this.distancesDisplayed = distancesDisplayed;
	}

	public Grid2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	public void runPathFinder(int pathStartCell, int pathTargetCell) {
		bfs = new BreadthFirstTraversal(grid);
		bfs.traverseGraph(pathStartCell, pathTargetCell);
		path = bfs.findPath(pathTargetCell)::iterator;
	}

	public Iterable<Integer> getPath() {
		return path;
	}

	private void addGridLayer() {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnPassageWidth = () -> cellSize * 9 / 10;
		renderer.fnCellSize = () -> cellSize;
		pushLayer(Layers.Grid.name(), g -> renderer.drawGrid(g, grid));
	}

	private void addDistanceLayer() {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnPassageWidth = () -> cellSize * 9 / 10;
		renderer.fnText = cell -> cellSize / 2 < renderer.getMinFontSize() ? ""
				: String.format("%d", bfs.getDistance(cell));
		renderer.fnTextFont = () -> new Font("Sans", Font.PLAIN, cellSize / 2);
		renderer.fnCellBgColor = cell -> {
			if (bfs.getMaxDistance() == -1) {
				return Color.BLACK;
			}
			float hue = 0.16f;
			if (bfs.getMaxDistance() > 0) {
				hue += 0.7f * bfs.getDistance(cell) / bfs.getMaxDistance();
			}
			return Color.getHSBColor(hue, 0.5f, 1f);
		};
		renderer.fnCellSize = () -> cellSize;
		pushLayer(Layers.Distances.name(), g -> {
			if (bfs != null) {
				renderer.drawGrid(g, grid);
			}
		});
		getLayer(Layers.Distances.name()).ifPresent(layer -> layer.setVisible(distancesDisplayed));
	}

	private void addPathLayer() {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnPassageWidth = () -> Math.max(cellSize * 10 / 100, 1);
		renderer.fnCellBgColor = cell -> Color.RED;
		renderer.fnCellSize = () -> cellSize;
		pushLayer(Layers.Path.name(), g -> {
			if (path != null) {
				Integer from = null;
				for (Integer cell : path) {
					if (from == null) {
						from = cell;
					} else {
						renderer.drawPassage(g, grid, grid.edge(from, cell).get(), true);
						from = cell;
					}
				}
			}
		});
		getLayer(Layers.Path.name()).ifPresent(layer -> layer.setVisible(pathDisplayed));
	}

	// implement GraphObserver interface

	@Override
	public void vertexChanged(VertexChangeEvent event) {
		repaint();
	}

	@Override
	public void edgeAdded(EdgeAddedEvent<WeightedEdge<Integer>> event) {
		repaint();
	}

	@Override
	public void edgeRemoved(EdgeRemovedEvent<WeightedEdge<Integer>> event) {
		repaint();
	}

	@Override
	public void edgeChanged(EdgeChangeEvent<WeightedEdge<Integer>> event) {
		repaint();
	}

	@Override
	public void graphChanged(ObservableGraph<WeightedEdge<Integer>> graph) {
		repaint();
	}

}