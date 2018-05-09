package de.amr.easy.maze.alg.ust;

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
		addToTree(start);
		List<Integer> walkStarts = grid.vertexStream().filter(v -> v != start).boxed()
				.collect(toCollection(ArrayList::new));
		while (!walkStarts.isEmpty()) {
			int walkStart = walkStarts.remove(rnd.nextInt(walkStarts.size()));
			loopErasedRandomWalk(walkStart);
		}
	}
}