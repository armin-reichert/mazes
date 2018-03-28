package de.amr.demos.maze.swing.layered;

import java.awt.Color;
import java.awt.Dimension;

import de.amr.demos.grid.swing.core.DefaultGridRenderingModel;
import de.amr.demos.grid.swing.core.GridRenderer;
import de.amr.demos.grid.swing.ui.LayeredCanvas;
import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.ObservableGrid;

public class GridCanvas extends LayeredCanvas {

	private int cellSize;
	private Grid2D<TraversalState, Integer> grid;
	private BreadthFirstTraversal<Integer, ?> bfs;
	private Iterable<Integer> path;

	public GridCanvas(int width, int height, int cellSize) {
		super(width, height);
		this.cellSize = cellSize;
		setBackground(Color.BLACK);
		newGrid();
		addGridLayer();
		addDistanceLayer();
		addPathLayer();
	}

	private void newGrid() {
		Dimension size = getSize();
		int rows = size.height / cellSize, cols = size.width / cellSize;
		grid = new ObservableGrid<>(cols, rows, TraversalState.UNVISITED);
	}

	public void setCellSize(int cellSize) {
		if (this.cellSize != cellSize) {
			this.cellSize = cellSize;
			newGrid();
			layers.clear();
			addGridLayer();
			addDistanceLayer();
			addPathLayer();
		}
	}

	public Grid2D<TraversalState, Integer> getGrid() {
		return grid;
	}

	public BreadthFirstTraversal<Integer, ?> getBfs() {
		return bfs;
	}

	public void setBfs(BreadthFirstTraversal<Integer, ?> bfs) {
		this.bfs = bfs;
	}

	public Iterable<Integer> getPath() {
		return path;
	}

	public void setPath(Iterable<Integer> path) {
		this.path = path;
	}

	private void addGridLayer() {
		DefaultGridRenderingModel gridRenderModel = new DefaultGridRenderingModel() {

			@Override
			public int getPassageWidth() {
				return cellSize * 9 / 10;
			}
		};
		gridRenderModel.setCellSize(cellSize);
		GridRenderer gridRenderer = new GridRenderer(gridRenderModel);
		pushLayer("gridLayer", g -> {
			gridRenderer.drawGrid(g, grid);
		});
	}

	private void addDistanceLayer() {
		DefaultGridRenderingModel gridRenderModel = new DefaultGridRenderingModel() {

			@Override
			public int getPassageWidth() {
				return cellSize * 9 / 10;
			}

			@Override
			public String getText(int cell) {
				return "" + bfs.getDistance(cell);
			}

			@Override
			public Color getCellBgColor(int cell) {
				if (bfs.getMaxDistance() == -1) {
					return super.getCellBgColor(cell);
				}
				float hue = 0.16f;
				if (bfs.getMaxDistance() > 0) {
					hue += 0.7f * bfs.getDistance(cell) / bfs.getMaxDistance();
				}
				return Color.getHSBColor(hue, 0.5f, 1f);
			}

		};
		gridRenderModel.setCellSize(cellSize);
		GridRenderer gridRenderer = new GridRenderer(gridRenderModel);
		pushLayer("distancesLayer", g -> {
			if (bfs != null) {
				gridRenderer.drawGrid(g, grid);
			}
		});
	}

	private void addPathLayer() {
		DefaultGridRenderingModel pathRenderModel = new DefaultGridRenderingModel() {

			@Override
			public Color getCellBgColor(int cell) {
				return Color.RED;
			}
		};
		pathRenderModel.setCellSize(cellSize);
		GridRenderer pathRenderer = new GridRenderer(pathRenderModel);
		pushLayer("pathLayer", g -> {
			if (path != null) {
				Integer from = null;
				for (Integer cell : path) {
					if (from == null) {
						from = cell;
					} else {
						pathRenderer.drawPassage(g, grid, grid.edge(from, cell).get(), true);
						from = cell;
					}
				}
			}
		});
	}

}
