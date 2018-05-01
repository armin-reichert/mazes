package de.amr.easy.maze.alg.wilson;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Wilson's algorithm with randomly selected start cells of the random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	public WilsonUSTRandomCell(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		List<Integer> startCells = grid.vertexStream().boxed().collect(toCollection(ArrayList<Integer>::new));
		startCells.remove((Object) start);
		addToTree(start);
		while (!startCells.isEmpty()) {
			loopErasedRandomWalk(startCells.remove(rnd.nextInt(startCells.size())));
		}
	}
}