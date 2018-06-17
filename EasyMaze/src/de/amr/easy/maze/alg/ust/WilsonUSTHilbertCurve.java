package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.util.GraphUtils;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Hilbert curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTHilbertCurve extends WilsonUST {

	private final int[] walkStartCells;

	public WilsonUSTHilbertCurve(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
		walkStartCells = new int[grid.numCells()];
		int n = GraphUtils.nextPow(2, max(grid.numCols(), grid.numRows()));
		BareGrid2D<?> square = new BareGrid<>(n, n, grid.getTopology(), SimpleEdge::new);
		int cell = square.cell(TOP_LEFT);
		int i = 0;
		walkStartCells[i++] = cell;
		for (int dir : new HilbertCurve(GraphUtils.log(2, n), W, N, E, S)) {
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