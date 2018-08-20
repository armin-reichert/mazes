package de.amr.easy.grid.ui.swing.animation;

import static java.lang.String.format;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import de.amr.easy.graph.api.event.GraphTraversalObserver;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
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
public class BreadthFirstTraversalAnimation {

	/**
	 * Runs a "flood-fill" on the given grid.
	 * 
	 * @param canvas
	 *                 grid canvas
	 * @param grid
	 *                 grid
	 * @param source
	 *                 cell where flood-fill starts
	 */
	public static void floodFill(GridCanvas canvas, GridGraph<?, ?> grid, int source) {
		floodFill(canvas, grid, source, true);
	}

	/**
	 * Runs a "flood-fill" on the given grid.
	 * 
	 * @param canvas
	 *                          grid canvas
	 * @param grid
	 *                          grid
	 * @param source
	 *                          cell where flood-fill starts
	 * @param distanceVisible
	 *                          if distances should be displayed as text
	 */
	public static void floodFill(GridCanvas canvas, GridGraph<?, ?> grid, int source, boolean distanceVisible) {
		BreadthFirstTraversal<?, ?> bfs = new BreadthFirstTraversal<>(grid);
		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.setDistanceVisible(distanceVisible);
		anim.run(canvas, bfs, source, -1);
	}

	private final GridGraph<?, ?> grid;
	private ConfigurableGridRenderer floodFillRenderer;
	private boolean distanceVisible;

	public IntSupplier fnDelay = () -> 0;
	public Supplier<Color> fnPathColor = () -> Color.RED;

	public BreadthFirstTraversalAnimation(GridGraph<?, ?> grid) {
		this.grid = grid;
		distanceVisible = true;
	}

	public boolean isDistanceVisible() {
		return distanceVisible;
	}

	public void setDistanceVisible(boolean visible) {
		this.distanceVisible = visible;
	}

	public Color getPathColor() {
		return fnPathColor.get();
	}

	/**
	 * @param canvas
	 *                 grid canvas displaying the animation
	 * @param source
	 *                 the source cell
	 * @param target
	 *                 the target cell
	 */
	public void run(GridCanvas canvas, BreadthFirstTraversal<?, ?> bfs, int source, int target) {
		canvas.getRenderer().ifPresent(canvasRenderer -> {
			// 1. traverse complete graph for computing maximum distance from source
			BreadthFirstTraversal<?, ?> distanceMap = new BreadthFirstTraversal<>(grid);
			distanceMap.traverseGraph(source);

			// Create renderer using distance map for coloring
			floodFillRenderer = createFloodFillRenderer(canvasRenderer, distanceMap);
			canvas.pushRenderer(floodFillRenderer);

			// 2. traverse again with events enabled
			GraphTraversalObserver canvasUpdater = new GraphTraversalObserver() {

				private void delayed(Runnable code) {
					if (fnDelay.getAsInt() > 0) {
						try {
							Thread.sleep(fnDelay.getAsInt());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					code.run();
					canvas.repaint();
				}

				@Override
				public void edgeTraversed(int either, int other) {
					delayed(() -> canvas.drawGridPassage(either, other, true));
				}

				@Override
				public void vertexTraversed(int v, TraversalState oldState, TraversalState newState) {
					delayed(() -> canvas.drawGridCell(v));
				}
			};
			bfs.addObserver(canvasUpdater);
			bfs.traverseGraph(source, target);
			bfs.removeObserver(canvasUpdater);
			canvas.popRenderer();
		});
	}

	public void showPath(GridCanvas canvas, BreadthFirstTraversal<?, ?> bfs, int target) {
		canvas.getRenderer().ifPresent(canvasRenderer -> {
			Iterable<Integer> path = bfs.path(target);
			if (floodFillRenderer != null) {
				canvas.pushRenderer(createPathRenderer(floodFillRenderer, bfs, path));
			} else {
				if (canvas.getRenderer().get() instanceof ConfigurableGridRenderer) {
					canvas.pushRenderer(createPathRenderer((ConfigurableGridRenderer) canvas.getRenderer().get(), bfs, path));
				} else {
					throw new IllegalStateException();
				}
			}
			path.forEach(canvas::drawGridCell);
			canvas.popRenderer();
		});
	}

	private ConfigurableGridRenderer createFloodFillRenderer(GridRenderer base, BreadthFirstTraversal<?, ?> distanceMap) {
		ConfigurableGridRenderer r = base instanceof PearlsGridRenderer ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellBgColor = cell -> colorByDist(cell, distanceMap);
		r.fnCellSize = base.getModel()::getCellSize;
		r.fnGridBgColor = () -> base.getModel().getGridBgColor();
		r.fnPassageColor = (u, v) -> colorByDist(u, distanceMap);
		r.fnPassageWidth = base.getModel()::getPassageWidth;
		r.fnText = cell -> distanceVisible ? format("%d", distanceMap.getDistFromSource(cell)) : "";
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnTextColor = cell -> Color.BLACK;
		return r;
	}

	private ConfigurableGridRenderer createPathRenderer(ConfigurableGridRenderer base,
			BreadthFirstTraversal<?, ?> distanceMap, Iterable<Integer> path) {
		BitSet inPath = new BitSet();
		path.forEach(inPath::set);
		ConfigurableGridRenderer r = base instanceof PearlsGridRenderer ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellBgColor = cell -> inPath.get(cell) ? getPathColor() : base.getCellBgColor(cell);
		r.fnCellSize = () -> base.getCellSize();
		r.fnGridBgColor = () -> base.getGridBgColor();
		r.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			return inPath.get(cell) && inPath.get(neighbor) ? getPathColor() : base.getCellBgColor(cell);
		};
		r.fnPassageWidth = () -> base.getPassageWidth() > 5 ? base.getPassageWidth() / 2 : base.getPassageWidth();
		r.fnText = cell -> distanceVisible ? format("%d", distanceMap.getDistFromSource(cell)) : "";
		r.fnTextFont = () -> new Font(Font.SANS_SERIF, Font.PLAIN, r.getPassageWidth() / 2);
		r.fnTextColor = cell -> Color.WHITE;
		return r;
	}

	private static Color colorByDist(int cell, BreadthFirstTraversal<?, ?> distanceMap) {
		float hue = 0.16f;
		if (distanceMap.getMaxDistFromSource() > 0) {
			hue += 0.7f * distanceMap.getDistFromSource(cell) / distanceMap.getMaxDistFromSource();
		}
		return Color.getHSBColor(hue, 0.5f, 1f);
	}
}