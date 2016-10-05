package de.amr.mazes.swing.rendering;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Set;

import de.amr.easy.graph.traversal.DepthFirstTraversal;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.rendering.DefaultGridRenderingModel;
import de.amr.easy.grid.rendering.GridCanvas;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class DFSAnimation {

	private final GridCanvas<Integer, ?> canvas;
	private final ObservableGrid2D<Integer, ?> grid;
	private final DFSRenderingModel renderingModel;
	private final DepthFirstTraversal<Integer, ?> dfs;
	private final Set<Integer> path;

	public DFSAnimation(GridCanvas<Integer, ?> canvas, ObservableGrid2D<Integer, ?> grid, Integer source,
			Integer target) {
		this.canvas = canvas;
		this.grid = grid;
		this.renderingModel = new DFSRenderingModel(canvas.currentRenderingModel().getCellSize(),
				canvas.currentRenderingModel().getPassageThickness(), Color.RED, Color.BLUE);
		dfs = new DepthFirstTraversal<>(grid, source, target);
		path = new LinkedHashSet<Integer>();
	}

	public void runAnimation() {
		canvas.pushRenderingModel(renderingModel);
		path.clear();
		for (Integer cell : dfs.findPath(dfs.getTarget())) {
			path.add(cell);
		}
		for (Integer cell : path) {
			canvas.renderGridCell(cell);
		}
		canvas.popRenderingModel();
	}

	// -- Rendering model

	private class DFSRenderingModel extends DefaultGridRenderingModel<Integer> {

		private final Color pathColor;
		private final Color visitColor;
		private final int cellSize;
		private final int passageThickness;

		public DFSRenderingModel(int cellSize, int passageThickness, Color pathColor, Color visitColor) {
			this.cellSize = cellSize;
			this.passageThickness = passageThickness > 5 ? passageThickness / 2 : passageThickness;
			this.pathColor = pathColor;
			this.visitColor = visitColor;
		}

		@Override
		public int getCellSize() {
			return cellSize;
		}

		@Override
		public int getPassageThickness() {
			return passageThickness;
		}

		@Override
		public Color getCellBgColor(Integer cell) {
			if (path.contains(cell)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED || dfs.isOnStack(cell)) {
				return visitColor;
			}
			return super.getCellBgColor(cell);
		}

		@Override
		public Color getPassageColor(Integer cell, Direction dir) {
			Integer neighbor = grid.neighbor(cell, dir);
			if (path.contains(cell) && path.contains(neighbor)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED && dfs.getState(neighbor) == VISITED) {
				return visitColor;
			}
			if (getCellBgColor(cell) == visitColor && getCellBgColor(neighbor) == visitColor) {
				return visitColor;
			}
			return super.getCellBgColor(cell);
		}
	};
}