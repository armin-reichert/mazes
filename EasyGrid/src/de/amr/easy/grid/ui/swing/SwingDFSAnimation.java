package de.amr.easy.grid.ui.swing;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.function.Function;

import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class SwingDFSAnimation implements GraphTraversalListener {

	private final AnimatedGridCanvas canvas;
	private final ObservableBareGrid2D<?> grid;
	private final DFSRenderingModel renderingModel;
	private final DepthFirstTraversal dfs;
	private final LinkedHashSet<Integer> path;

	public SwingDFSAnimation(AnimatedGridCanvas canvas, ObservableBareGrid2D<?> grid, Integer source, Integer target) {
		this.canvas = canvas;
		this.grid = grid;
		GridRenderingModel currentModel = canvas.getRenderingModel();
		this.renderingModel = new DFSRenderingModel(currentModel.getCellSize(), currentModel.getPassageWidth(),
				currentModel::getCellBgColor, Color.RED, Color.BLUE);
		dfs = new DepthFirstTraversal(grid, source, target);
		path = new LinkedHashSet<>();
	}

	public void run() {
		dfs.addObserver(this);
		canvas.pushRenderingModel(renderingModel);
		path.clear();
		dfs.traverseGraph();
		dfs.findPath(dfs.getTarget()).forEach(path::add);
		path.forEach(cell -> canvas.drawGridCell(cell));
		canvas.popRenderingModel();
		dfs.removeObserver(this);
	}

	@Override
	public void edgeTouched(int source, int target) {
		canvas.drawGridPassage(grid.edge(source, target).get(), true);
	}

	@Override
	public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
		canvas.drawGridCell(vertex);
	}

	// -- Rendering model

	private class DFSRenderingModel extends DefaultGridRenderingModel {

		private final Function<Integer, Color> baseCellColorFunction;
		private final Color pathColor;
		private final Color visitedCellColor;

		public DFSRenderingModel(int cellSize, int passageWidth, Function<Integer, Color> baseCellColorFunction,
				Color pathColor, Color visitedCellColor) {
			this.cellSize = cellSize;
			this.passageWidth = passageWidth > 5 ? passageWidth / 2 : passageWidth;
			this.baseCellColorFunction = baseCellColorFunction;
			this.pathColor = pathColor;
			this.visitedCellColor = visitedCellColor;
		}

		@Override
		public Color getCellBgColor(int cell) {
			if (path.contains(cell)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED || dfs.isStacked(cell)) {
				return visitedCellColor;
			}
			return baseCellColorFunction.apply(cell);
		}

		@Override
		public Color getPassageColor(int cell, int dir) {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			if (path.contains(cell) && path.contains(neighbor)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED && dfs.getState(neighbor) == VISITED) {
				return visitedCellColor;
			}
			if (getCellBgColor(cell) == visitedCellColor && getCellBgColor(neighbor) == visitedCellColor) {
				return visitedCellColor;
			}
			return baseCellColorFunction.apply(cell);
		}
	};
}