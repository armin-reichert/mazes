package de.amr.mazes.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.impl.ObservableDataGrid;

public class CurveUtil {

	public static void followCurve(ObservableDataGrid<TraversalState> grid, Iterable<Direction> curve, Integer startCell,
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
