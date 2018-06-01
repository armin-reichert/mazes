package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Animation of breadth-first-search with path finding. Shows the distances as the BFS traverses the
 * graph and colors the cells according to their distance from the source ("flood-fill").
 * 
 * @author Armin Reichert
 */
public class SwingBFSAnimation {

	private final BareGrid2D<?> grid;
	private final GridCanvas<?> canvas;
	private BreadthFirstTraversal bfs;
	private ConfigurableGridRenderer renderer;
	private int maxDistance;
	private int maxDistanceCell;
	private boolean distancesVisible;
	private Color pathColor;

	public SwingBFSAnimation(BareGrid2D<?> grid, GridCanvas<?> canvas) {
		this.grid = grid;
		this.canvas = canvas;
		maxDistance = -1;
		maxDistanceCell = -1;
		distancesVisible = true;
		pathColor = Color.RED;
		createRenderer(canvas.getRenderer().get(), new BitSet());
	}

	private void createRenderer(GridRenderer oldRenderer, BitSet inPath) {

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

		renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = oldRenderer::getPassageWidth;
		renderer.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, renderer.getPassageWidth() / 2);
		renderer.fnText = cell -> distancesVisible && bfs.getDistance(cell) != -1 ? "" + bfs.getDistance(cell) : "";
		renderer.fnCellBgColor = cell -> inPath.get(cell) ? pathColor : distanceColor.apply(cell);
		renderer.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			return inPath.get(cell) && inPath.get(neighbor) ? pathColor : distanceColor.apply(cell);
		};
	}

	public void run(BreadthFirstTraversal bfs, int target) {
		bfs.setTarget(target);
		run(bfs);
	}

	public void run(BreadthFirstTraversal bfs) {
		this.bfs = bfs;

		// 1. run without events for computing maximum distance (cell) from source
		bfs.traverseGraph();
		maxDistance = bfs.getMaxDistance();
		maxDistanceCell = bfs.getMaxDistanceVertex();

		// 2. run with events
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

	public void showPath(int target) {
		if (bfs == null) {
			throw new IllegalStateException("Must run BFS before showing path");
		}
		int[] path = bfs.findPath(target).toArray();
		BitSet inPath = new BitSet();
		IntStream.of(path).forEach(inPath::set);
		createRenderer(canvas.getRenderer().get(), inPath);
		int passageWidth = renderer.getPassageWidth();
		renderer.fnPassageWidth = () -> passageWidth > 5 ? passageWidth / 2 : passageWidth;
		renderer.fnTextColor = () -> Color.WHITE;
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