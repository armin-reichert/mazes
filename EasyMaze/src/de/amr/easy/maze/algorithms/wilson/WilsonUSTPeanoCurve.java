package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.maze.misc.Utils.log;
import static de.amr.easy.maze.misc.Utils.nextPow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.iterators.traversals.PeanoCurve;

/**
 * Wilson's algorithm where the vertices are selected from a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private final List<Integer> path = new ArrayList<>();

	public WilsonUSTPeanoCurve(ObservableDataGrid2D<Integer, TraversalState> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		int n = nextPow(3, Math.max(grid.numCols(), grid.numRows()));
		Grid squareGrid = new Grid(n, n);
		Integer cell = squareGrid.cell(BOTTOM_LEFT);
		addCellToPath(squareGrid.col(cell), squareGrid.row(cell));
		for (Direction d : new PeanoCurve(log(3, n))) {
			cell = squareGrid.neighbor(cell, d);
			addCellToPath(squareGrid.col(cell), squareGrid.row(cell));
		}
		return path.stream();
	}

	private void addCellToPath(int x, int y) {
		if (grid.isValidCol(x) && grid.isValidRow(y)) {
			path.add(grid.cell(x, y));
		}
	}
}