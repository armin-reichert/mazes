package de.amr.easy.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.traversals.RecursiveCrosses;

/**
 * Wilson's algorithm where the vertices are selected from recursive crosses.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRecursiveCrosses extends WilsonUST {

	public WilsonUSTRecursiveCrosses(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	protected IntStream cellStream() {
		return new RecursiveCrosses(grid).stream();
	}
}