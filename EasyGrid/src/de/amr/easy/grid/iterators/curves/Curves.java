package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.impl.ObservableGrid;

/**
 * Utility class for curves.
 * 
 * @author Armin Reichert
 *
 */
public class Curves {

	/**
	 * Walks the given curve on the given grid, starting at the given cell.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          a grid
	 * @param start
	 *          the start cell of the walk
	 */
	public static void walk(Curve curve, ObservableGrid<TraversalState, ?> grid, Integer start) {
		walk(curve, grid, start, () -> {
		});
	}

	/**
	 * Walks the given curve on the given grid, starting at the given cell and execute the given
	 * action at each step.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          a grid
	 * @param start
	 *          the start cell of the walk
	 * @param action
	 *          code to be executed at each step of the walk
	 */
	public static void walk(Curve curve, ObservableGrid<TraversalState, ?> grid, Integer start, Runnable action) {
		Integer current = start;
		grid.set(current, COMPLETED);
		for (Direction dir : curve) {
			Integer next = grid.neighbor(current, dir).get();
			grid.addEdge(current, next);
			action.run();
			current = next;
			grid.set(current, COMPLETED);
		}
	}
}
