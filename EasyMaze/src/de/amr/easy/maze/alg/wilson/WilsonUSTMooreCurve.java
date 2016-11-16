package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.maze.misc.MazeUtils.log;
import static de.amr.easy.maze.misc.MazeUtils.nextPow;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.impl.BareGrid;

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
	protected IntStream cellStream() {
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		MooreLCurve moore = new MooreLCurve(log(2, n));
		BareGrid<?> square = new BareGrid<>(n, n);
		Integer cell = square.cell(n / 2, n - 1);
		addCellToPath(n / 2, n - 1);
		for (int dir : moore) {
			cell = square.neighbor(cell, dir).getAsInt();
			addCellToPath(square.col(cell), square.row(cell));
		}
		return path.stream().mapToInt(Integer::intValue); // TODO
	}

	private void addCellToPath(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			path.add(grid.cell(col, row));
		}
	}
}