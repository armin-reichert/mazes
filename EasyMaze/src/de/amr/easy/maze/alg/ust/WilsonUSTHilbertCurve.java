package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static de.amr.easy.util.GridUtils.log;
import static de.amr.easy.util.GridUtils.nextPow;
import static java.lang.Math.max;

import java.util.Arrays;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.grid.impl.Top4;

/**
 * Wilson's algorithm where the vertices are selected from a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTHilbertCurve extends WilsonUST {

	public WilsonUSTHilbertCurve(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		int[] path = new int[grid.numCells()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		HilbertCurve hilbert = new HilbertCurve(log(2, n), W, N, E, S);
		// Hilbert curve needs a square grid, so create one
		BareGrid<?> square = new BareGrid<>(n, n, Top4.get());
		// Traverse the intersection of the square grid cells with the original grid
		int cell = square.cell(TOP_LEFT);
		int i = 0;
		path[i++] = cell;
		for (int dir : hilbert.dirs()) {
			// Hilbert curve never leaves the square grid thus neighbor always exists:
			cell = square.neighbor(cell, dir).getAsInt();
			// Add cell if it is inside original grid:
			int col = square.col(cell), row = square.row(cell);
			if (grid.isValidCol(col) && grid.isValidRow(row)) {
				path[i++] = grid.cell(col, row);
			}
		}
		return Arrays.stream(path);
	}
}