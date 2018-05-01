package de.amr.easy.maze.alg.wilson;

import static java.util.stream.Collectors.toList;

import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Wilson's algorithm with randomly selected start cells of the random walks.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	private List<Integer> startCells;

	public WilsonUSTRandomCell(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		startCells = grid.vertexStream().boxed().collect(toList());
		addToTree(start);
		while (!startCells.isEmpty()) {
			loopErasedRandomWalk(startCells.get(rnd.nextInt(startCells.size())));
		}
	}

	@Override
	protected void addToTree(int cell) {
		super.addToTree(cell);
		startCells.remove((Object) cell); // remove(int) is the wrong method!
	}
}