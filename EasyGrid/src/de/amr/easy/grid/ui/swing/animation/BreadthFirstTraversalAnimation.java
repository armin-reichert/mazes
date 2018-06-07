package de.amr.easy.grid.ui.swing.animation;

import static java.lang.String.format;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;

/**
 * Animation of breadth-first traversal. Shows the distances from the source cell while the BFS
 * traverses the graph and colors the cells according to their distance ("flood-fill").
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
		new BreadthFirstTraversalAnimation<>(new BreadthFirstTraversal<>(grid)).run(canvas, source, -1);
	}

	private final G grid;
	private final BreadthFirstTraversal<G> bfs;
	private GridRenderer floodFillRenderer;
	private boolean distanceVisible;
	private Color pathColor;

	public BreadthFirstTraversalAnimation(BreadthFirstTraversal<G> bfs) {
		this.bfs = bfs;
		grid = bfs.getGraph();
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
	public void run(ObservingGridCanvas canvas, int source, int target) {
		canvas.getRenderer().ifPresent(canvasRenderer -> {

			// 1. traverse complete graph for computing maximum distance from source
			BreadthFirstTraversal<G> distanceMeter = new BreadthFirstTraversal<>(grid);
			canvas.stopListening();
			distanceMeter.traverseGraph(source);
			canvas.startListening();

			// Create renderer from distance measurement results
			floodFillRenderer = createFloodFillRenderer(canvasRenderer, distanceMeter);
			canvas.pushRenderer(floodFillRenderer);

			// 2. traverse again with events enabled
			GraphTraversalListener bfsObserver = new GraphTraversalListener() {

				@Override
				public void edgeTouched(int source, int target) {
					canvas.drawGridPassage(grid.edge(source, target).get(), true);
				}

				@Override
				public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
					canvas.drawGridCell(vertex);
				}
			};
			bfs.addObserver(bfsObserver);
			bfs.traverseGraph(source, target);
			bfs.removeObserver(bfsObserver);
			canvas.popRenderer();
		});
	}

	private GridRenderer createFloodFillRenderer(GridRenderer base, BreadthFirstTraversal<?> distanceMeter) {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = base.getModel()::getCellSize;
		renderer.fnPassageWidth = base.getModel()::getPassageWidth;
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, renderer.getPassageWidth() / 2);
		renderer.fnTextFont = () -> font;
		renderer.fnText = cell -> distanceVisible ? format("%d", bfs.getDistance(cell)) : "";
		renderer.fnCellBgColor = cell -> colorByDist(cell, distanceMeter);
		renderer.fnPassageColor = (u, v) -> colorByDist(u, distanceMeter);
		return renderer;
	}

	public void showPath(ObservingGridCanvas canvas, int target) {
		if (bfs == null) {
			throw new IllegalStateException();
		}
		canvas.getRenderer().ifPresent(canvasRenderer -> {
			int[] path = bfs.findPath(target).toArray();
			GridRenderer renderer = createPathRenderer(floodFillRenderer, path);
			canvas.pushRenderer(renderer);
			IntStream.of(path).forEach(canvas::drawGridCell);
			canvas.popRenderer();
		});
	}

	private GridRenderer createPathRenderer(GridRenderer baseRenderer, int[] path) {
		ConfigurableGridRenderer r = new ConfigurableGridRenderer();
		BitSet inPath = new BitSet();
		IntStream.of(path).forEach(inPath::set);
		r.fnCellBgColor = cell -> inPath.get(cell) ? pathColor : baseRenderer.getModel().getCellBgColor(cell);
		r.fnCellSize = () -> baseRenderer.getModel().getCellSize();
		r.fnGridBgColor = () -> baseRenderer.getModel().getGridBgColor();
		r.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			return inPath.get(cell) && inPath.get(neighbor) ? pathColor : baseRenderer.getModel().getCellBgColor(cell);
		};
		r.fnPassageWidth = () -> baseRenderer.getModel().getPassageWidth() > 5
				? baseRenderer.getModel().getPassageWidth() / 2
				: baseRenderer.getModel().getPassageWidth();
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnText = cell -> distanceVisible && bfs.getDistance(cell) != -1 ? "" + bfs.getDistance(cell) : "";
		r.fnTextColor = () -> Color.WHITE;
		return r;
	}

	private static Color colorByDist(int cell, BreadthFirstTraversal<?> distanceMeter) {
		float hue = 0.16f;
		if (distanceMeter.getMaxDistance() > 0) {
			hue += 0.7f * distanceMeter.getDistance(cell) / distanceMeter.getMaxDistance();
		}
		return Color.getHSBColor(hue, 0.5f, 1f);
	}
}