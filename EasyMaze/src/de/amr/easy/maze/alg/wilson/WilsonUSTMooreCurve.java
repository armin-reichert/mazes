package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.maze.misc.Utils.log;
import static de.amr.easy.maze.misc.Utils.nextPow;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.NakedGrid;
import de.amr.easy.grid.iterators.curves.MooreCurve;

/**
 * Wilson's algorithm where the vertices are selected from a Hilbert-Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	private final List<Integer> path = new ArrayList<>();

	public WilsonUSTMooreCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected Stream<Integer> cellStream() {
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		MooreCurve moore = new MooreCurve(log(2, n));
		NakedGrid<?> square = new NakedGrid<>(n, n);
		Integer cell = square.cell(n / 2, n - 1);
		addCellToPath(n / 2, n - 1);
		for (Direction dir : moore) {
			cell = square.neighbor(cell, dir).get();
			addCellToPath(square.col(cell), square.row(cell));
		}
		return path.stream();
	}

	private void addCellToPath(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			path.add(grid.cell(col, row));
		}
	}
}