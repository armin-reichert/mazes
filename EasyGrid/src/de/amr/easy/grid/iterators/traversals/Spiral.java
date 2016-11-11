package de.amr.easy.grid.iterators.traversals;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.api.Sequence;
import de.amr.easy.grid.impl.NakedGrid;
import de.amr.easy.grid.iterators.shapes.Square;

/**
 * A sequence of cells starting at the center of the grid and expanding like a spiral until all grid
 * cells are traversed.
 * 
 * @author Armin Reichert
 */
public class Spiral implements Sequence<Integer> {

	private final List<Integer> cells = new ArrayList<>();

	public Spiral(NakedGrid2D<?> grid, Integer start) {
		int size = Math.max(grid.numCols(), grid.numRows());
		int offsetY = (size - grid.numRows()) / 2;
		NakedGrid2D<?> squareGrid = new NakedGrid<>(size, size);
		Integer leftUpperCorner = squareGrid.cell(CENTER);
		for (int i = 0, n = size / 2 + 1; i < n; ++i) {
			Square square = new Square(squareGrid, leftUpperCorner, 2 * i + 1);
			for (Integer cell : square) {
				int x = squareGrid.col(cell);
				int y = squareGrid.row(cell) - offsetY;
				if (grid.isValidCol(x) && grid.isValidRow(y)) {
					cells.add(grid.cell(x, y));
				}
			}
			if (i < n - 1) {
				leftUpperCorner = squareGrid.cell(squareGrid.col(leftUpperCorner) - 1, squareGrid.row(leftUpperCorner) - 1);
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
