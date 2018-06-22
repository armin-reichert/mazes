package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static de.amr.easy.util.GraphUtils.log;
import static de.amr.easy.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.impl.GridGraph;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTHilbertCurve extends WilsonUST {

	private final int[] walkStartCells;

	public WilsonUSTHilbertCurve(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
		walkStartCells = new int[grid.numVertices()];
		int n = nextPow(2, max(grid.numCols(), grid.numRows()));
		GridGraph<?,?> square = new GridGraph<>(n, n, grid.getTopology(), SimpleEdge::new);
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
	}

	@Override
	protected IntStream cellStream() {
		return stream(walkStartCells);
	}
}