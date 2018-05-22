package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.util.GridUtils.log;
import static de.amr.easy.util.GridUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.grid.impl.Top4;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Peano curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private final int[] walkStartCells;
	private final BareGrid<?> square;
	private final PeanoCurve peano;
	private int i;

	public WilsonUSTPeanoCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
		walkStartCells = new int[grid.numCells()];
		int n = nextPow(3, max(grid.numCols(), grid.numRows()));
		square = new BareGrid<>(n, n, Top4.get());
		peano = new PeanoCurve(log(3, n));
		int current = square.cell(BOTTOM_LEFT);
		addWalkStartCell(square.col(current), square.row(current));
		for (int dir : peano) {
			current = square.neighbor(current, dir).getAsInt();
			addWalkStartCell(square.col(current), square.row(current));
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