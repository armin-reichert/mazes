package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.GraphUtils.log;
import static de.amr.easy.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.grid.curves.Curve;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	public WilsonUSTMooreCurve(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int[] walkStartCells = new int[maze.numVertices()];
		int n = nextPow(2, max(maze.numCols(), maze.numRows()));
		OrthogonalGrid square = emptyGrid(n, n, UNVISITED);
		Curve mooreCurve = new MooreLCurve(log(2, n));
		int cell = square.cell(n / 2, n - 1);
		int i = 0;
		if (maze.isValidCol(n / 2) && maze.isValidRow(n - 1)) {
			walkStartCells[i++] = maze.cell(n / 2, n - 1);
		}
		for (int dir : mooreCurve) {
			cell = square.neighbor(cell, dir).getAsInt();
			int col = square.col(cell), row = square.row(cell);
			if (maze.isValidCol(col) && maze.isValidRow(row)) {
				walkStartCells[i++] = maze.cell(col, row);
			}
		}
		return stream(walkStartCells);
	}
}