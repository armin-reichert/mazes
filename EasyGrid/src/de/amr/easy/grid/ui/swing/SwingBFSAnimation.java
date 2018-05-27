package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Animation of breadth-first-search with path finding. Shows the distances as the BFS traverses the
 * graph and colors the cells according to their distance from the source ("flood-fill").
 * 
 * @author Armin Reichert
 */
public class SwingBFSAnimation {

	private final BareGrid2D<?> grid;
	private BreadthFirstTraversal bfs;
	private int maxDistance;
	private int maxDistanceCell;
	private boolean distancesVisible;
	private Color pathColor;

	public SwingBFSAnimation(BareGrid2D<?> grid) {
		this.grid = grid;
		maxDistance = -1;
		maxDistanceCell = -1;
		distancesVisible = true;
		pathColor = Color.RED;
	}

	private ConfigurableGridRenderer createRenderer(GridRenderer oldRenderer, BitSet inPath) {

		Function<Integer, Color> distanceColor = cell -> {
			if (maxDistance == -1) {
				return oldRenderer.getCellBgColor(cell);
			}
			float hue = 0.16f;
			if (maxDistance > 0) {
				hue += 0.7f * bfs.getDistance(cell) / maxDistance;
			}
			return Color.getHSBColor(hue, 0.5f, 1f);
		};

		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = oldRenderer::getPassageWidth;
		renderer.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, renderer.getPassageWidth() / 2);
		renderer.fnText = cell -> distancesVisible && bfs.getDistance(cell) != -1 ? "" + bfs.getDistance(cell) : "";
		renderer.fnCellBgColor = cell -> inPath.get(cell) ? pathColor : distanceColor.apply(cell);
		renderer.fnPassageColor = (cell, dir) -> {
			if (inPath.get(cell)) {
				OptionalInt neighbor = grid.neighbor(cell, dir);
				if (neighbor.isPresent()) {
					if (inPath.get(neighbor.getAsInt())) {
						return pathColor;
					}
				}
			}
			return distanceColor.apply(cell);
		};
		return renderer;
	}

	public void runBFSAnimation(GridCanvas<?> canvas, int source) {
		// 1. run without events for computing maximum distance (cell) from source
		bfs = new BreadthFirstTraversal(grid, source);
		bfs.traverseGraph();
		maxDistance = bfs.getMaxDistance();
		maxDistanceCell = bfs.getMaxDistanceVertex();

		// 2. run with events
		ConfigurableGridRenderer renderer = createRenderer(canvas.getRenderer().get(), new BitSet());
		canvas.pushRenderer(renderer);
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
		bfs.traverseGraph();
		canvas.popRenderer();
	}

	public void showPath(GridCanvas<?> canvas, int target) {
		if (bfs == null) {
			throw new IllegalStateException("Must run BFS before showing path");
		}
		int[] path = bfs.findPath(target).toArray();
		BitSet inPath = new BitSet();
		IntStream.of(path).forEach(inPath::set);
		ConfigurableGridRenderer renderer = createRenderer(canvas.getRenderer().get(), inPath);
		int smallerPassageWidth = renderer.getPassageWidth() / 2;
		renderer.fnPassageWidth = () -> smallerPassageWidth;
		renderer.fnText = cell -> "";
		canvas.pushRenderer(renderer);
		IntStream.of(path).forEach(canvas::drawGridCell);
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