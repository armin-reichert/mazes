package de.amr.easy.maze.alg.wilson;

import static de.amr.easy.maze.misc.MazeUtils.log;
import static de.amr.easy.maze.misc.MazeUtils.nextPow;
import static java.lang.Math.max;

import java.util.Arrays;
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

	private final int[] path;
	private int i;

	public WilsonUSTMooreCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
		path = new int[grid.numCells()];
		i = 0;
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
		return Arrays.stream(path);
	}

	private void addCellToPath(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			path[i++] = grid.cell(col, row);
		}
	}
}