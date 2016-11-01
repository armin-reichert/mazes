package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.impl.ObservableGrid;

public class CurveUtil {

	public static void walkCurve(ObservableGrid<TraversalState, ?> grid, Iterable<Direction> curve, Integer startCell,
			Runnable edgeAddedAction) {
		Integer current = startCell;
		grid.set(current, COMPLETED);
		for (Direction dir : curve) {
			Integer next = grid.neighbor(current, dir).get();
			grid.addEdge(current, next);
			edgeAddedAction.run();
			current = next;
			grid.set(current, COMPLETED);
		}
	}
}
