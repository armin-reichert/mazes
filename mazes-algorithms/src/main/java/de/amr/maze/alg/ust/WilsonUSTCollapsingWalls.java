package de.amr.maze.alg.ust;

import java.util.stream.IntStream;

import de.amr.graph.grid.traversals.CollapsingWalls;
import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Wilson's algorithm where the vertices are selected alternating left-to-right and right-to-left
 * column-wise.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTCollapsingWalls extends WilsonUST {

	public WilsonUSTCollapsingWalls(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return new CollapsingWalls(grid).stream();
	}
}