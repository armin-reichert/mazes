package de.amr.easy.grid.rendering.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.OptionalInt;
import java.util.Set;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Animation of breadth-first-search path finding. Shows the distances as the BFS traverses the
 * graph and colors the cells according to their distance from the source.
 * 
 * @author Armin Reichert
 */
public class BFSAnimation implements GraphTraversalListener<Integer> {

	private final SwingGridCanvas canvas;
	private final ObservableBareGrid2D<?> grid;
	private final BFSRenderingModel renderingModel;
	private final Set<Integer> path;
	private BreadthFirstTraversal<Integer, ?> bfs;
	private int maxDistance;
	private int maxDistanceCell;
	private boolean distancesVisible;

	public BFSAnimation(SwingGridCanvas canvas, ObservableBareGrid2D<?> grid) {
		this.canvas = canvas;
		this.grid = grid;
		renderingModel = new BFSRenderingModel(canvas.currentRenderingModel().getCellSize(),
				canvas.currentRenderingModel().getPassageThickness(), Color.RED);
		path = new LinkedHashSet<>();
		maxDistance = -1;
		distancesVisible = true;
	}

	public void runAnimation(int source) {
		// 1. run BFS silently to compute maximum distance from source:
		canvas.stopListening();
		bfs = new BreadthFirstTraversal<>(grid, source);
		bfs.run();
		maxDistance = bfs.getMaxDistance();
		maxDistanceCell = bfs.getMaxDistanceVertex();
		canvas.startListening();

		// 2. run BFS with events enabled such that coloring and distances are
		// rendered:
		canvas.pushRenderingModel(renderingModel);
		bfs.addObserver(this);
		bfs.run();
		bfs.removeObserver(this);
		canvas.popRenderingModel();
	}

	public void showPath(int target) {
		path.clear();
		for (int cell : bfs.findPath(target)) {
			path.add(cell);
		}
		canvas.pushRenderingModel(renderingModel);
		for (int cell : path) {
			canvas.renderGridCell(cell);
		}
		canvas.popRenderingModel();
	}

	public int getMaxDistanceCell() {
		return maxDistanceCell;
	}

	public boolean isDistancesVisible() {
		return distancesVisible;
	}

	public void setDistancesVisible(boolean distancesVisible) {
		this.distancesVisible = distancesVisible;
	}

	// GraphTraversalListener

	@Override
	public void edgeTouched(Integer source, Integer target) {
		canvas.renderGridPassage(grid.edge(source, target).get(), true);
	}

	@Override
	public void vertexTouched(Integer vertex, TraversalState oldState, TraversalState newState) {
		canvas.renderGridCell(vertex);
	}

	// -- Rendering model

	private class BFSRenderingModel extends SwingDefaultGridRenderingModel {

		private final int cellSize;
		private final int passageThickness;
		private final Color pathColor;
		private Font textFont;

		public BFSRenderingModel(int cellSize, int passageThickness, Color pathColor) {
			this.cellSize = cellSize;
			this.passageThickness = passageThickness;
			this.pathColor = pathColor;
		}

		@Override
		public int getCellSize() {
			return cellSize;
		}

		@Override
		public String getCellText(int cell) {
			return distancesVisible && bfs.getDistance(cell) != -1 ? String.valueOf(bfs.getDistance(cell)) : "";
		}

		@Override
		public Color getCellBgColor(int cell) {
			return path.contains(cell) ? pathColor : cellColor(cell);
		}

		@Override
		public int getPassageThickness() {
			return passageThickness;
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
			return cellColor(cell);
		}

		@Override
		public Font getCellTextFont() {
			if (textFont == null || textFont.getSize() > getPassageThickness() / 2) {
				textFont = new Font("SansSerif", Font.PLAIN, getPassageThickness() / 2);
			}
			return textFont;
		}

		private Color cellColor(int cell) {
			if (maxDistance == -1) {
				return renderingModel.getCellBgColor(cell);
			}
			float hue = 0.16f;
			if (maxDistance != 0) {
				hue += 0.7f * bfs.getDistance(cell) / maxDistance;
			}
			return Color.getHSBColor(hue, 0.5f, 1f);
		}
	};
}