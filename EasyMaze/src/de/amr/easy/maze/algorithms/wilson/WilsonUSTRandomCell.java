package de.amr.easy.maze.algorithms.wilson;

import java.util.LinkedList;
import java.util.List;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Wilson's algorithm with random vertex selection.
 * 
 * @author Armin Reichert
 */
public class WilsonUSTRandomCell extends WilsonUST {

	private final List<Integer> cellsOutsideTree;

	public WilsonUSTRandomCell(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		super(grid);
		cellsOutsideTree = new LinkedList<>();
		grid.vertexStream().forEach(cell -> {
			cellsOutsideTree.add(cell);
		});
	}

	@Override
	public void accept(Integer start) {
		addCellToTree(start);
		while (!cellsOutsideTree.isEmpty()) {
			loopErasedRandomWalk(pickRandomCellOutsideTree());
		}
	}

	@Override
	protected void addCellToTree(Integer v) {
		super.addCellToTree(v);
		cellsOutsideTree.remove(v);
	}

	protected void addEdge(Integer v, Integer w) {
		grid.addEdge(new DefaultEdge<>(v, w));
		cellsOutsideTree.remove(v);
		cellsOutsideTree.remove(w);
	}

	private Integer pickRandomCellOutsideTree() {
		return cellsOutsideTree.get(rnd.nextInt(cellsOutsideTree.size()));
	}
}
