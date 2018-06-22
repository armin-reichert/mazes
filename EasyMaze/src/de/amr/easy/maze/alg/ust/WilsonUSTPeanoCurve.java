package de.amr.easy.maze.alg.ust;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.util.GraphUtils.log;
import static de.amr.easy.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.curves.Curve;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.Top4;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Peano curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private final int[] walkStartCells;
	private int i;

	public WilsonUSTPeanoCurve(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
		walkStartCells = new int[grid.numVertices()];
		int n = nextPow(3, max(grid.numCols(), grid.numRows()));
		GridGraph<?, ?> square = new GridGraph<>(n, n, Top4.get(), SimpleEdge::new);
		Curve peano = new PeanoCurve(log(3, n));
		int current = square.cell(BOTTOM_LEFT);
		addWalkStartCell(square.col(current), square.row(current));
		for (int dir : peano) {
			current = square.neighbor(current, dir).getAsInt();
			addWalkStartCell(square.col(current), square.row(current));
		}
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return stream(walkStartCells);
	}

	private void addWalkStartCell(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			walkStartCells[i++] = grid.cell(col, row);
		}
	}
}