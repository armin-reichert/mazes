package de.amr.easy.maze.alg.ust;

import static de.amr.easy.util.GraphUtils.log;
import static de.amr.easy.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.grid.curves.Curve;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Moore curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTMooreCurve extends WilsonUST {

	private final int[] walkStartCells;

	public WilsonUSTMooreCurve(OrthogonalGrid grid) {
		super(grid);
		walkStartCells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		GridGraph<?, ?> square = new GridGraph<>(n, n, Top4.get(), SimpleEdge::new);
		Curve mooreCurve = new MooreLCurve(log(2, n));
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
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return stream(walkStartCells);
	}
}