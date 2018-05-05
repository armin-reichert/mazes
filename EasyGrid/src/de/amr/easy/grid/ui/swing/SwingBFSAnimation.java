package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.OptionalInt;
import java.util.function.Function;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Animation of breadth-first-search with path finding. Shows the distances as the BFS traverses the
 * graph and colors the cells according to their distance from the source.
 * 
 * @author Armin Reichert
 */
public class SwingBFSAnimation implements GraphTraversalListener {

	private final AnimatedGridCanvas canvas;
	private final ObservableBareGrid2D<?> grid;
	private final BFSRenderingModel bfsRenderingModel;
	private final LinkedHashSet<Integer> path;
	private BreadthFirstTraversal<?> bfs;
	private int maxDistance;
	private int maxDistanceCell;
	private boolean distancesVisible;

	public SwingBFSAnimation(AnimatedGridCanvas canvas, ObservableBareGrid2D<?> grid) {
		this.canvas = canvas;
		this.grid = grid;
		GridRenderingModel currentModel = canvas.getRenderingModel();
		bfsRenderingModel = new BFSRenderingModel(currentModel.getCellSize(), currentModel.getPassageWidth(),
				currentModel::getCellBgColor, Color.RED);
		path = new LinkedHashSet<>();
		maxDistance = -1;
		maxDistanceCell = -1;
		distancesVisible = true;
	}

	public void run(int startCell) {
		// 1. run silently for computing maximum distance from start cell:
		canvas.stopListening();
		bfs = new BreadthFirstTraversal<>(grid, startCell);
		bfs.traverseGraph();
		maxDistance = bfs.getMaxDistance();
		maxDistanceCell = bfs.getMaxDistanceVertex();

		// 2. run with events such that coloring and distances get rendered:
		canvas.startListening();
		canvas.pushRenderingModel(bfsRenderingModel);
		bfs.addObserver(this);
		bfs.traverseGraph();
		bfs.removeObserver(this);
		canvas.popRenderingModel();
	}

	public void showPath(int targetCell) {
		path.clear();
		bfs.findPath(targetCell).forEach(path::add);
		canvas.pushRenderingModel(bfsRenderingModel);
		path.forEach(canvas::drawGridCell);
		canvas.popRenderingModel();
	}

	public int getMaxDistanceCell() {
		return maxDistanceCell;
	}

	public boolean areDistancesVisible() {
		return distancesVisible;
	}

	public void setDistancesVisible(boolean distancesVisible) {
		this.distancesVisible = distancesVisible;
	}

	// GraphTraversalListener

	@Override
	public void edgeTouched(int source, int target) {
		canvas.drawGridPassage(grid.edge(source, target).get(), true);
	}

	@Override
	public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
		canvas.drawGridCell(vertex);
	}

	// -- Rendering model

	private class BFSRenderingModel extends DefaultGridRenderingModel {

		private final Function<Integer, Color> baseCellColorFunction;
		private final Color pathColor;

		public BFSRenderingModel(int cellSize, int passageWidth, Function<Integer, Color> baseCellColorFunction,
				Color pathColor) {
			this.cellSize = cellSize;
			this.passageWidth = passageWidth;
			this.baseCellColorFunction = baseCellColorFunction;
			this.pathColor = pathColor;
			this.textFont = new Font("SansSerif", Font.PLAIN, passageWidth / 2);
		}

		@Override
		public String getText(int cell) {
			if (distancesVisible) {
				int distance = bfs.getDistance(cell);
				if (distance != -1) {
					return String.valueOf(distance);
				}
			}
			return "";
		}

		@Override
		public Color getCellBgColor(int cell) {
			return path.contains(cell) ? pathColor : cellColorByDistance(cell);
		}

		@Override
		public Color getPassageColor(int cell, int dir) {
			if (path.contains(cell)) {
				OptionalInt neighbor = grid.neighbor(cell, dir);
				if (neighbor.isPresent()) {
					if (path.contains(neighbor.getAsInt())) {
						return pathColor;
					}
				}
			}
			return cellColorByDistance(cell);
		}

		private Color cellColorByDistance(int cell) {
			if (maxDistance == -1) {
				return baseCellColorFunction.apply(cell);
			}
			float hue = 0.16f;
			if (maxDistance > 0) {
				hue += 0.7f * bfs.getDistance(cell) / maxDistance;
			}
			return Color.getHSBColor(hue, 0.5f, 1f);
		}
	};
}