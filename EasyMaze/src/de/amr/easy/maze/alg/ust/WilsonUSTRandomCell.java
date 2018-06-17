package de.amr.easy.maze.alg.ust;

import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Wilson's algorithm with random start cells of the loop-erased random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	public WilsonUSTRandomCell(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		addToTree(start);
		permute(grid.vertices()).forEach(this::loopErasedRandomWalk);
	}
}