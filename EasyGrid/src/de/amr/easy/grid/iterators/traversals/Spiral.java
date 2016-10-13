package de.amr.easy.grid.iterators.traversals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.iterators.shapes.Square;

public class Spiral implements Sequence<Integer> {

	private final List<Integer> cells = new ArrayList<>();

	public Spiral(Grid2D grid, Integer start) {
		int size = Math.max(grid.numCols(), grid.numRows());
		int offsetY = (size - grid.numRows()) / 2;
		Grid2D quadraticGrid = new Grid(size, size);
		Integer leftUpperCorner = quadraticGrid.cell(GridPosition.CENTER);
		for (int i = 0, n = size / 2 + 1; i < n; ++i) {
			Square square = new Square(quadraticGrid, leftUpperCorner, 2 * i + 1);
			for (Integer cell : square) {
				int x = quadraticGrid.col(cell);
				int y = quadraticGrid.row(cell) - offsetY;
				if (grid.isValidCol(x) && grid.isValidRow(y)) {
					cells.add(grid.cell(x, y));
				}
			}
			if (i < n - 1) {
				leftUpperCorner = quadraticGrid.cell(quadraticGrid.col(leftUpperCorner) - 1,
						quadraticGrid.row(leftUpperCorner) - 1);
			}
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return cells.iterator();
	}

	@Override
	public Stream<Integer> stream() {
		return cells.stream();
	}
}
