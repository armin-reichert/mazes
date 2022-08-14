package de.amr.maze.alg.ust;

import static de.amr.graph.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.datastruct.StreamUtils;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.traversals.Spiral;

/**
 * Wilson's algorithm where the vertices are selected from an expanding spiral.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTExpandingSpiral extends WilsonUST {

	public WilsonUSTExpandingSpiral(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		runWilsonAlgorithm(grid.cell(CENTER));
	}

	@Override
	protected IntStream randomWalkStartCells() {
		return StreamUtils.toIntStream(new Spiral(grid));
	}
}