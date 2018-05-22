package de.amr.easy.maze.alg.ust;

import static de.amr.easy.util.GridUtils.log;
import static de.amr.easy.util.GridUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Curve;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.grid.impl.Top4;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	private final int[] walkStartCells;
	private final BareGrid<?> square;
	private final Curve mooreCurve;
	private int i;
	private int n;

	public WilsonUSTMooreCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
		walkStartCells = new int[grid.numCells()];
		n = nextPow(2, max(grid.numCols(), grid.numRows()));
		square = new BareGrid<>(n, n, Top4.get());
		mooreCurve = new MooreLCurve(log(2, n));
		int cell = square.cell(n / 2, n - 1);
		i = 0;
		addWalkStartCell(n / 2, n - 1);
		for (int dir : mooreCurve) {
			cell = square.neighbor(cell, dir).getAsInt();
			addWalkStartCell(square.col(cell), square.row(cell));
		}
	}

	@Override
	protected IntStream cellStream() {
		return stream(walkStartCells);
	}

	private void addWalkStartCell(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			walkStartCells[i++] = grid.cell(col, row);
		}
	}
}