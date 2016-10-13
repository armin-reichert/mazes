package de.amr.easy.maze.algorithms.wilson;

import java.util.List;
import java.util.stream.Collectors;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Wilson's algorithm with random vertex selection.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	private final List<Integer> cellsOutsideTree;

	public WilsonUSTRandomCell(ObservableDataGrid2D<Integer, TraversalState> grid) {
		super(grid);
		cellsOutsideTree = grid.vertexStream().collect(Collectors.toList());
	}

	@Override
	public void accept(Integer start) {
		addCellToTree(start);
		while (!cellsOutsideTree.isEmpty()) {
			loopErasedRandomWalk(cellsOutsideTree.get(rnd.nextInt(cellsOutsideTree.size())));
		}
	}

	@Override
	protected void addCellToTree(Integer v) {
		super.addCellToTree(v);
		cellsOutsideTree.remove(v);
	}
}