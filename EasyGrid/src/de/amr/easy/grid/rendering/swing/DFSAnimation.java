package de.amr.easy.grid.rendering.swing;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Set;

import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.ObservableNakedGrid2D;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class DFSAnimation implements GraphTraversalListener<Integer> {

	private final SwingGridCanvas canvas;
	private final ObservableNakedGrid2D<Dir4,?> grid;
	private final DFSRenderingModel renderingModel;
	private final DepthFirstTraversal<Integer, ?> dfs;
	private final Set<Integer> path;

	public DFSAnimation(SwingGridCanvas canvas, ObservableNakedGrid2D<Dir4,?> grid, Integer source, Integer target) {
		this.canvas = canvas;
		this.grid = grid;
		this.renderingModel = new DFSRenderingModel(canvas.currentRenderingModel().getCellSize(),
				canvas.currentRenderingModel().getPassageThickness(), Color.RED, Color.BLUE);
		dfs = new DepthFirstTraversal<>(grid, source, target);
		path = new LinkedHashSet<>();
	}

	public void runAnimation() {
		dfs.addObserver(this);
		canvas.pushRenderingModel(renderingModel);
		path.clear();
		for (Integer cell : dfs.findPath(dfs.getTarget())) {
			path.add(cell);
		}
		for (Integer cell : path) {
			canvas.renderGridCell(cell);
		}
		canvas.popRenderingModel();
		dfs.removeObserver(this);
	}

	@Override
	public void edgeTouched(Integer source, Integer target) {
		canvas.renderGridPassage(grid.edge(source, target).get(), true);
	}

	@Override
	public void vertexTouched(Integer vertex, TraversalState oldState, TraversalState newState) {
		canvas.renderGridCell(vertex);
	}

	// -- Rendering model

	private class DFSRenderingModel extends SwingDefaultGridRenderingModel {

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
			if (dfs.getState(cell) == VISITED || dfs.onStack(cell)) {
				return visitColor;
			}
			return super.getCellBgColor(cell);
		}

		@Override
		public Color getPassageColor(Integer cell, Dir4 dir) {
			Integer neighbor = grid.neighbor(cell, dir).get();
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