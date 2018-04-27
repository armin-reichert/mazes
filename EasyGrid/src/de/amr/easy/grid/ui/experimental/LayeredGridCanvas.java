package de.amr.easy.grid.ui.experimental;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.ui.swing.DefaultGridRenderingModel;
import de.amr.easy.grid.ui.swing.GridRenderer;

/**
 * Canvas that can display a grid, a colored distance map and a path between two cells.
 * 
 * @author Armin Reichert
 */
public class LayeredGridCanvas extends LayeredCanvas {

	private int cellSize;
	private boolean pathDisplayed;
	private boolean distancesDisplayed;
	private Grid2D<TraversalState, Integer> grid;
	private BreadthFirstTraversal<?> bfs;
	private Iterable<Integer> path;

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

	private void newGrid() {
		Dimension size = getSize();
		int rows = size.height / cellSize, cols = size.width / cellSize;
		grid = new ObservableGrid<>(cols, rows, TraversalState.UNVISITED);
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
		bfs = new BreadthFirstTraversal<>(grid, pathStartCell);
		bfs.run();
		path = bfs.findPath(pathTargetCell);
	}

	public Iterable<Integer> getPath() {
		return path;
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
				return cellSize / 2 < getMinFontSize() ? "" : String.format("%d", bfs.getDistance(cell));
			}

			@Override
			public Font getTextFont() {
				return new Font("Sans", Font.PLAIN, cellSize / 2);
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
		getLayer("distancesLayer").ifPresent(layer -> layer.setVisible(distancesDisplayed));
	}

	private void addPathLayer() {
		DefaultGridRenderingModel pathRenderModel = new DefaultGridRenderingModel() {

			@Override
			public int getPassageWidth() {
				return Math.max(cellSize * 10 / 100, 1);
			}

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
		getLayer("pathLayer").ifPresent(layer -> layer.setVisible(pathDisplayed));
	}
}
