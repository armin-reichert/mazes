package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.util.GraphUtils.log;
import static de.amr.graph.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.curves.MooreLCurve;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	public WilsonUSTMooreCurve(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int[] walkStartCells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		GridGraph2D<TraversalState, Integer> square = factory.emptyGrid(n, n, UNVISITED);
		MooreLCurve mooreCurve = new MooreLCurve(log(2, n));
		int cell = square.cell(n / 2, n - 1);
		int i = 0;
		if (grid.isValidCol(n / 2) && grid.isValidRow(n - 1)) {
			walkStartCells[i++] = grid.cell(n / 2, n - 1);
		}
		for (int dir : mooreCurve) {
			cell = square.neighbor(cell, dir).getAsInt();
			int col = square.col(cell), row = square.row(cell);
			if (grid.isValidCol(col) && grid.isValidRow(row)) {
				walkStartCells[i++] = grid.cell(col, row);
			}
		}
		return stream(walkStartCells);
	}
}