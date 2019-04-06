package de.amr.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.graph.grid.traversals.RecursiveCrosses;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected from recursive crosses.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRecursiveCrosses extends WilsonUST {

	public WilsonUSTRecursiveCrosses(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new RecursiveCrosses(grid).stream();
	}
}