package de.amr.easy.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.graph.grid.impl.OrthogonalGrid.emptyGrid;
import static de.amr.graph.grid.impl.Top4.E;
import static de.amr.graph.grid.impl.Top4.N;
import static de.amr.graph.grid.impl.Top4.S;
import static de.amr.graph.grid.impl.Top4.W;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.graph.util.GraphUtils.log;
import static de.amr.graph.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.graph.grid.curves.HilbertCurve;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTHilbertCurve extends WilsonUST {

	public WilsonUSTHilbertCurve(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int[] walkStartCells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		OrthogonalGrid square = emptyGrid(n, n, UNVISITED);
		int cell = square.cell(TOP_LEFT);
		int i = 0;
		walkStartCells[i++] = cell;
		for (int dir : new HilbertCurve(log(2, n), W, N, E, S)) {
			cell = square.neighbor(cell, dir).getAsInt();
			int col = square.col(cell), row = square.row(cell);
			if (grid.isValidCol(col) && grid.isValidRow(row)) {
				walkStartCells[i++] = grid.cell(col, row);
			}
		}
		return stream(walkStartCells);
	}
}