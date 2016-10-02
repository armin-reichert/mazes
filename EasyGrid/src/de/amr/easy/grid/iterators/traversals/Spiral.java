package de.amr.easy.grid.iterators.traversals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.RawGrid;
import de.amr.easy.grid.iterators.shapes.Square;

public class Spiral<Cell> implements Iterable<Cell> {

	private final List<Cell> cells = new ArrayList<Cell>();

	public Spiral(Grid2D<Cell, ?> grid, Cell start) {
		int size = Math.max(grid.numCols(), grid.numRows());
		int offsetY = (size - grid.numRows()) / 2;
		Grid2D<Integer, DefaultEdge<Integer>> squareGrid = new RawGrid(size, size);
		Integer luc = squareGrid.cell(GridPosition.CENTER);
		for (int i = 0, n = size / 2 + 1; i < n; ++i) {
			Square<Integer> square = new Square<Integer>(squareGrid, luc, 2 * i + 1);
			for (Integer cell : square) {
				int x = squareGrid.col(cell);
				int y = squareGrid.row(cell) - offsetY;
				if (grid.isValidCol(x) && grid.isValidRow(y)) {
					cells.add(grid.cell(x, y));
				}
			}
			if (i < n - 1) {
				luc = squareGrid.cell(squareGrid.col(luc) - 1, squareGrid.row(luc) - 1);
			}
		}
	}

	@Override
	public Iterator<Cell> iterator() {
		return cells.iterator();
	}
}
