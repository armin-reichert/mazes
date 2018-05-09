package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.util.GridUtils.nextPow;
import static java.lang.Math.max;

import java.util.Arrays;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.util.GridUtils;

/**
 * Wilson's algorithm where the vertices are selected from a Peano-curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private final int[] path;
	private int i;

	public WilsonUSTPeanoCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
		path = new int[grid.numCells()];
	}

	@Override
	protected IntStream cellStream() {
		int n = nextPow(3, max(grid.numCols(), grid.numRows()));
		PeanoCurve peano = new PeanoCurve(GridUtils.log(3, n));
		BareGrid<?> square = new BareGrid<>(n, n, Topologies.TOP4);
		Integer cell = square.cell(BOTTOM_LEFT);
		addCellToPath(square.col(cell), square.row(cell));
		for (int dir : peano) {
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