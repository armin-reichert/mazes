package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.permute;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;

/**
 * Wilson's algorithm with random start cells of the loop-erased random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	public WilsonUSTRandomCell(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return permute(grid.vertices());
	}
}