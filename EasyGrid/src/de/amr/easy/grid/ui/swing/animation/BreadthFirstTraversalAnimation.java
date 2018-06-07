package de.amr.easy.grid.ui.swing.animation;

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
public class BreadthFirstTraversalAnimation {

	private final BareGrid2D<?> grid;
	private final BreadthFirstTraversal bfs;
	private GridRenderer floodFillRenderer;
	private boolean distanceVisible;
	private Color pathColor;

	public BreadthFirstTraversalAnimation(BareGrid2D<?> grid, BreadthFirstTraversal bfs) {
		this.grid = grid;
		this.bfs = bfs;
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
			BreadthFirstTraversal distanceMeter = new BreadthFirstTraversal(grid);
			distanceMeter.traverseGraph(source);

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

	private GridRenderer createFloodFillRenderer(GridRenderer baseRenderer, BreadthFirstTraversal distanceMeter) {
		ConfigurableGridRenderer r = new ConfigurableGridRenderer();
		r.fnCellBgColor = cell -> colorByDist(cell, distanceMeter);
		r.fnCellSize = () -> baseRenderer.getModel().getCellSize();
		r.fnGridBgColor = () -> baseRenderer.getModel().getGridBgColor();
		r.fnPassageColor = (cell, dir) -> colorByDist(cell, distanceMeter);
		r.fnPassageWidth = () -> baseRenderer.getModel().getPassageWidth();
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnText = cell -> distanceVisible && bfs.getDistance(cell) != -1 ? "" + distanceMeter.getDistance(cell) : "";
		return r;
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

	private static Color colorByDist(int cell, BreadthFirstTraversal distanceMeter) {
		float hue = 0.16f;
		if (distanceMeter.getMaxDistance() > 0) {
			hue += 0.7f * distanceMeter.getDistance(cell) / distanceMeter.getMaxDistance();
		}
		return Color.getHSBColor(hue, 0.5f, 1f);
	}
}