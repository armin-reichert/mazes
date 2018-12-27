package de.amr.easy.maze.alg.ust;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.OrthogonalGrid;

/**
 * Wilson's algorithm with random start cells of the loop-erased random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	public WilsonUSTRandomCell(int numCols, int numRows) {
		super(numCols, numRows);
	}

	public WilsonUSTRandomCell(OrthogonalGrid grid) {
		super(grid);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return permute(grid.vertices());
	}
}