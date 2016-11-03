package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.iterators.curves.Curve;

public class CurveUtil {

	public static void walk(Curve curve, ObservableGrid<TraversalState, ?> grid, Integer start) {
		walk(curve, grid, start, () -> {
		});
	}

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
