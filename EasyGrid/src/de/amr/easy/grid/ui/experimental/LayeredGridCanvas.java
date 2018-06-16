package de.amr.easy.grid.ui.experimental;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexEvent;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

/**
 * Canvas that can display a grid, a colored distance map and a path between two cells.
 * 
 * @author Armin Reichert
 */
public class LayeredGridCanvas<E extends Edge> extends LayeredCanvas implements GraphObserver {

	private enum Layers {
		Grid, Distances, Path
	};

	private final BiFunction<Integer, Integer, E> fnEdgeFactory;

	protected int cellSize;
	protected boolean pathDisplayed;
	protected boolean distancesDisplayed;
	protected ObservableGrid2D<TraversalState, E> grid;
	protected BreadthFirstTraversal bfs;
	protected int maxDistance;
	protected Iterable<Integer> path;

	public LayeredGridCanvas(int width, int height, int cellSize, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(width, height);
		this.fnEdgeFactory = fnEdgeFactory;
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
		grid = new ObservableGrid<>(cols, rows, Top4.get(), TraversalState.UNVISITED, false, fnEdgeFactory);
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

	public Grid2D<TraversalState, E> getGrid() {
		return grid;
	}

	public void runPathFinder(int pathStartCell, int pathTargetCell) {
		bfs = new BreadthFirstTraversal(grid);
		bfs.traverseGraph(pathStartCell);
		maxDistance = bfs.getMaxDistance();
		bfs = new BreadthFirstTraversal(grid);
		bfs.traverseGraph(pathStartCell, pathTargetCell);
		path = bfs.findPath(pathTargetCell)::iterator;
	}

	public Iterable<Integer> getPath() {
		return path;
	}

	private void addGridLayer() {
		ConfigurableGridRenderer renderer = new WallPassageGridRenderer();
		renderer.fnPassageWidth = () -> cellSize * 9 / 10;
		renderer.fnCellSize = () -> cellSize;
		pushLayer(Layers.Grid.name(), g -> renderer.drawGrid(g, grid));
	}

	private void addDistanceLayer() {
		ConfigurableGridRenderer renderer = new WallPassageGridRenderer();
		renderer.fnPassageWidth = () -> cellSize * 9 / 10;
		renderer.fnText = cell -> cellSize / 2 < renderer.getMinFontSize() ? ""
				: String.format("%d", bfs.getDistance(cell));
		renderer.fnTextFont = () -> new Font("Sans", Font.PLAIN, cellSize / 2);
		renderer.fnCellBgColor = cell -> {
			if (maxDistance == -1) {
				return Color.BLACK;
			}
			float hue = 0.16f;
			if (maxDistance > 0) {
				hue += 0.7f * bfs.getDistance(cell) / maxDistance;
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
		ConfigurableGridRenderer renderer = new WallPassageGridRenderer();
		renderer.fnPassageWidth = () -> Math.max(cellSize * 10 / 100, 1);
		renderer.fnCellBgColor = cell -> Color.RED;
		renderer.fnCellSize = () -> cellSize;
		pushLayer(Layers.Path.name(), g -> {
			if (path != null) {
				Integer parent = null;
				for (Integer cell : path) {
					if (parent == null) {
						parent = cell;
					} else {
						renderer.drawPassage(g, grid, parent, cell, true);
						parent = cell;
					}
				}
			}
		});
		getLayer(Layers.Path.name()).ifPresent(layer -> layer.setVisible(pathDisplayed));
	}

	// implement GraphObserver interface

	@Override
	public void vertexChanged(VertexEvent event) {
		repaint();
	}

	@Override
	public void edgeAdded(EdgeEvent event) {
		repaint();
	}

	@Override
	public void edgeRemoved(EdgeEvent event) {
		repaint();
	}

	@Override
	public void edgeChanged(EdgeEvent event) {
		repaint();
	}

	@Override
	public void graphChanged(ObservableGraph<?> graph) {
		repaint();
	}
}