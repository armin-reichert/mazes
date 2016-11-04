package de.amr.easy.grid.iterators.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.api.ObservableGrid2D;

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
	public static void walk(Curve curve, ObservableGrid2D<TraversalState, ?> grid, Integer start) {
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
	public static void walk(Curve curve, ObservableGrid2D<TraversalState, ?> grid, Integer start, Runnable action) {
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

	public static Stream<Integer> points(Curve curve, NakedGrid2D<?> grid, Integer start) {
		List<Integer> points = new ArrayList<>();
		Integer current = start;
		points.add(current);
		for (Direction dir : curve) {
			Integer next = grid.neighbor(current, dir).get();
			points.add(next);
			current = next;
		}
		return points.stream();
	}

	public static String pointsAsString(Curve curve, NakedGrid2D<?> grid, Integer start) {
		return points(curve, grid, start).map(cell -> format("(%d,%d)", grid.col(cell), grid.row(cell))).collect(joining());
	}
}
