package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.OptionalInt;

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
public class SwingBFSAnimation {

	private final ObservableBareGrid2D<?> grid;
	private ConfigurableGridRenderer renderer;
	private int[] path;
	private BitSet inPath;
	private BreadthFirstTraversal bfs;
	private int maxDistance;
	private int maxDistanceCell;
	private boolean distancesVisible;
	private Color pathColor;

	public SwingBFSAnimation(ObservableBareGrid2D<?> grid) {
		this.grid = grid;
		this.inPath = new BitSet();
		maxDistance = -1;
		maxDistanceCell = -1;
		distancesVisible = true;
		pathColor = Color.RED;
	}

	private ConfigurableGridRenderer createRenderer(GridRenderer oldRenderer) {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = oldRenderer::getPassageWidth;
		renderer.fnTextFont = () -> new Font("SansSerif", Font.PLAIN, renderer.getPassageWidth() / 2);
		renderer.fnText = cell -> {
			if (distancesVisible) {
				int distance = bfs.getDistance(cell);
				if (distance != -1) {
					return String.valueOf(distance);
				}
			}
			return "";
		};
		renderer.fnCellBgColor = cell -> inPath.get(cell) ? pathColor : cellColorByDistance(cell, oldRenderer);
		renderer.fnPassageColor = (cell, dir) -> {
			if (inPath.get(cell)) {
				OptionalInt neighbor = grid.neighbor(cell, dir);
				if (neighbor.isPresent()) {
					if (inPath.get(neighbor.getAsInt())) {
						return pathColor;
					}
				}
			}
			return cellColorByDistance(cell, oldRenderer);
		};
		return renderer;
	}

	private Color cellColorByDistance(int cell, GridRenderer oldRenderer) {
		if (maxDistance == -1) {
			return oldRenderer.getCellBgColor(cell);
		}
		float hue = 0.16f;
		if (maxDistance > 0) {
			hue += 0.7f * bfs.getDistance(cell) / maxDistance;
		}
		return Color.getHSBColor(hue, 0.5f, 1f);
	}

	public void run(AnimatedGridCanvas canvas, int startCell) {
		renderer = createRenderer(canvas.getRenderer());
		
		bfs = new BreadthFirstTraversal(grid, startCell);
		bfs.addObserver(new GraphTraversalListener() {

			@Override
			public void edgeTouched(int source, int target) {
				canvas.drawGridPassage(grid.edge(source, target).get(), true);
			}

			@Override
			public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
				canvas.drawGridCell(vertex);
			}
		});

		// 1. silent run for computing maximum distance from start cell
		canvas.stopListening();
		bfs.traverseGraph();
		maxDistance = bfs.getMaxDistance();
		maxDistanceCell = bfs.getMaxDistanceVertex();

		// 2. run with publishing of events
		canvas.startListening();
		canvas.pushRenderer(renderer);
		bfs.traverseGraph();
		canvas.popRenderer();
	}

	public void showPath(AnimatedGridCanvas canvas, int target) {
		path = bfs.findPath(target).toArray();
		inPath = new BitSet();
		for (int cell : path) {
			inPath.set(cell);
		}
		canvas.pushRenderer(renderer);
		for (int cell : path) {
			canvas.drawGridCell(cell);
		}
		canvas.popRenderer();
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

	public Color getPathColor() {
		return pathColor;
	}

	public void setPathColor(Color pathColor) {
		this.pathColor = pathColor;
	}
}