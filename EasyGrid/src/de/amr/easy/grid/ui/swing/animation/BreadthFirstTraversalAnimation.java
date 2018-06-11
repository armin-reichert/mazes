package de.amr.easy.grid.ui.swing.animation;

import static java.lang.String.format;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

/**
 * Animation of breadth-first traversal on a grid.
 * <p>
 * Shows the distances from the source cell while the BFS traverses the graph and colors the cells
 * according to their distance ("flood-fill").
 * 
 * @author Armin Reichert
 */
public class BreadthFirstTraversalAnimation<G extends BareGrid2D<Integer>> {

	/**
	 * Runs a "flood-fill" on the given grid.
	 * 
	 * @param canvas
	 *          grid canvas
	 * @param grid
	 *          grid
	 * @param source
	 *          cell where flood-fill starts
	 */
	public static <G extends BareGrid2D<Integer>> void floodFill(ObservingGridCanvas canvas, G grid, int source) {
		floodFill(canvas, grid, source, true);
	}

	/**
	 * Runs a "flood-fill" on the given grid.
	 * 
	 * @param canvas
	 *          grid canvas
	 * @param grid
	 *          grid
	 * @param source
	 *          cell where flood-fill starts
	 * @param distanceVisible
	 *          if distances should be displayed as text
	 */
	public static <G extends BareGrid2D<Integer>> void floodFill(ObservingGridCanvas canvas, G grid, int source,
			boolean distanceVisible) {
		BreadthFirstTraversal<G> bfs = new BreadthFirstTraversal<>(grid);
		BreadthFirstTraversalAnimation<G> anim = new BreadthFirstTraversalAnimation<>(grid);
		anim.setDistanceVisible(distanceVisible);
		anim.run(canvas, bfs, source, -1);
	}

	private final G grid;
	private ConfigurableGridRenderer floodFillRenderer;
	private boolean distanceVisible;
	private Color pathColor;

	public BreadthFirstTraversalAnimation(G grid) {
		this.grid = grid;
		distanceVisible = true;
		pathColor = Color.RED;
	}

	public boolean isDistanceVisible() {
		return distanceVisible;
	}

	public void setDistanceVisible(boolean visible) {
		this.distanceVisible = visible;
	}

	public Color getPathColor() {
		return pathColor;
	}

	public void setPathColor(Color color) {
		this.pathColor = color;
	}

	/**
	 * @param canvas
	 *          grid canvas displaying the animation
	 * @param source
	 *          the source cell
	 * @param target
	 *          the target cell
	 */
	public void run(ObservingGridCanvas canvas, BreadthFirstTraversal<G> bfs, int source, int target) {
		canvas.getRenderer().ifPresent(canvasRenderer -> {
			// 1. traverse complete graph for computing maximum distance from source
			BreadthFirstTraversal<G> distanceMap = new BreadthFirstTraversal<>(grid);
			canvas.stopListening();
			distanceMap.traverseGraph(source);
			canvas.startListening();

			// Create renderer using distance map for coloring
			floodFillRenderer = createFloodFillRenderer(canvasRenderer, distanceMap);
			canvas.pushRenderer(floodFillRenderer);

			// 2. traverse again with events enabled
			GraphTraversalListener canvasUpdater = new GraphTraversalListener() {

				@Override
				public void edgeTouched(int u, int v) {
					grid.edge(u, v).ifPresent(edge -> canvas.drawGridPassage(edge, true));
				}

				@Override
				public void vertexTouched(int v, TraversalState oldState, TraversalState newState) {
					canvas.drawGridCell(v);
				}
			};
			bfs.addObserver(canvasUpdater);
			bfs.traverseGraph(source, target);
			bfs.removeObserver(canvasUpdater);
			canvas.popRenderer();
		});
	}

	public void showPath(ObservingGridCanvas canvas, BreadthFirstTraversal<G> bfs, int target) {
		canvas.getRenderer().ifPresent(canvasRenderer -> {
			int[] path = bfs.findPath(target).toArray();
			canvas.pushRenderer(createPathRenderer(floodFillRenderer, bfs, path));
			Arrays.stream(path).forEach(canvas::drawGridCell);
			canvas.popRenderer();
		});
	}

	private ConfigurableGridRenderer createFloodFillRenderer(GridRenderer base, BreadthFirstTraversal<?> distanceMap) {
		ConfigurableGridRenderer r = base instanceof PearlsGridRenderer ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellBgColor = cell -> colorByDist(cell, distanceMap);
		r.fnCellSize = base.getModel()::getCellSize;
		r.fnGridBgColor = () -> base.getModel().getGridBgColor();
		r.fnPassageColor = (u, v) -> colorByDist(u, distanceMap);
		r.fnPassageWidth = base.getModel()::getPassageWidth;
		r.fnText = cell -> distanceVisible ? format("%d", distanceMap.getDistance(cell)) : "";
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnTextColor = () -> Color.BLACK;
		return r;
	}

	private ConfigurableGridRenderer createPathRenderer(ConfigurableGridRenderer base,
			BreadthFirstTraversal<?> distanceMap, int[] path) {
		BitSet inPath = new BitSet();
		IntStream.of(path).forEach(inPath::set);
		ConfigurableGridRenderer r = base instanceof PearlsGridRenderer ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellBgColor = cell -> inPath.get(cell) ? pathColor : base.getCellBgColor(cell);
		r.fnCellSize = () -> base.getCellSize();
		r.fnGridBgColor = () -> base.getGridBgColor();
		r.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			return inPath.get(cell) && inPath.get(neighbor) ? pathColor : base.getCellBgColor(cell);
		};
		r.fnPassageWidth = () -> base.getPassageWidth() > 5 ? base.getPassageWidth() / 2 : base.getPassageWidth();
		r.fnText = cell -> distanceVisible ? format("%d", distanceMap.getDistance(cell)) : "";
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnTextColor = () -> Color.WHITE;
		return r;
	}

	private static Color colorByDist(int cell, BreadthFirstTraversal<?> distanceMap) {
		float hue = 0.16f;
		if (distanceMap.getMaxDistance() > 0) {
			hue += 0.7f * distanceMap.getDistance(cell) / distanceMap.getMaxDistance();
		}
		return Color.getHSBColor(hue, 0.5f, 1f);
	}
}