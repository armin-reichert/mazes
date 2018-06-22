package de.amr.easy.maze.alg.ust;

import static de.amr.easy.util.StreamUtils.permute;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;

/**
 * Wilson's algorithm with random start cells of the loop-erased random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	public WilsonUSTRandomCell(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return permute(grid.vertices());
	}
}