package de.amr.easy.maze.alg.wilson;

import java.util.List;
import java.util.stream.Collectors;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;

/**
 * Wilson's algorithm with random vertex selection.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	private final List<Integer> cellsOutsideTree;

	public WilsonUSTRandomCell(Grid2D<Dir4,TraversalState, Integer> grid) {
		super(grid);
		cellsOutsideTree = grid.vertexStream().collect(Collectors.toList());
	}

	@Override
	public void accept(Integer start) {
		addToTree(start);
		while (!cellsOutsideTree.isEmpty()) {
			loopErasedRandomWalk(cellsOutsideTree.get(rnd.nextInt(cellsOutsideTree.size())));
		}
	}

	@Override
	protected void addToTree(Integer v) {
		super.addToTree(v);
		cellsOutsideTree.remove(v);
	}
}