package de.amr.easy.grid.iterators.traversals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import de.amr.easy.grid.api.NakedGrid2D;

public class RecursiveCrosses implements Sequence<Integer> {

	private final NakedGrid2D grid;
	private final List<Integer> path = new ArrayList<>();

	public RecursiveCrosses(NakedGrid2D grid) {
		this.grid = grid;
		cross(grid.numCols(), grid.numRows(), 0, 0);
	}

	@Override
	public Iterator<Integer> iterator() {
		return path.iterator();
	}

	private void cross(int width, int height, int startCol, int startRow) {
		if (width <= 0 || height <= 0) {
			return;
		}
		int w2 = width / 2;
		int h2 = height / 2;
		if (2 * w2 < width) {
			++w2;
		} else if (2 * h2 < height) {
			++h2;
		}
		int centerCol = startCol + w2, centerRow = startRow + h2;
		// Vertical axis
		IntStream.range(startRow, startRow + height).forEach(row -> {
			if (grid.isValidCol(centerCol) && grid.isValidRow(row)) {
				path.add(grid.cell(centerCol, row));
			}
		});
		// Horizontal axis
		IntStream.range(startCol, startCol + width).forEach(col -> {
			if (grid.isValidCol(col) && grid.isValidRow(centerRow)) {
				path.add(grid.cell(col, centerRow));
			}
		});
		// Recursive calls
		cross(w2, h2, startCol, startRow);
		cross(w2, h2, centerCol, centerRow);
		cross(w2, h2, centerCol, startRow);
		cross(w2, h2, startCol, centerRow);
	}
}
