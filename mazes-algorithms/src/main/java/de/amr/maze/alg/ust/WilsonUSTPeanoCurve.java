package de.amr.maze.alg.ust;

import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.graph.grid.impl.GridFactory.emptyGrid;
import static de.amr.graph.util.GraphUtils.log;
import static de.amr.graph.util.GraphUtils.nextPow;
import static java.lang.Math.max;
import static java.util.Arrays.stream;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.curves.PeanoCurve;
import de.amr.graph.grid.impl.Grid4Topology;

/**
 * Wilson's algorithm where the random walks start in the order defined by a Peano curve.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTPeanoCurve extends WilsonUST {

	private int i;
	private int[] walkStartCells;

	public WilsonUSTPeanoCurve(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
		walkStartCells = new int[grid.numVertices()];
	}

	@Override
	protected IntStream randomWalkStartCells() {
		int n = nextPow(3, max(grid.numCols(), grid.numRows()));
		GridGraph2D<?, ?> squareGrid = emptyGrid(n, n, Grid4Topology.get(), UNVISITED, 0);
		int cell = squareGrid.cell(BOTTOM_LEFT);
		addCell(squareGrid.col(cell), squareGrid.row(cell));
		for (byte dir : new PeanoCurve(log(3, n))) {
			cell = squareGrid.neighbor(cell, dir).get();
			addCell(squareGrid.col(cell), squareGrid.row(cell));
		}
		return stream(walkStartCells);
	}

	private void addCell(int col, int row) {
		if (grid.isValidCol(col) && grid.isValidRow(row)) {
			walkStartCells[i++] = grid.cell(col, row);
		}
	}
}