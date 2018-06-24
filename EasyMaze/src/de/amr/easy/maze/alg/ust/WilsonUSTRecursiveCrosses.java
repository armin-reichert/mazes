package de.amr.easy.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.easy.grid.iterators.traversals.RecursiveCrosses;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm where the vertices are selected from recursive crosses.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRecursiveCrosses extends WilsonUST {

	public WilsonUSTRecursiveCrosses(int numCols, int numRows) {
		super(numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells(OrthogonalGrid maze) {
		return new RecursiveCrosses(maze).stream();
	}
}