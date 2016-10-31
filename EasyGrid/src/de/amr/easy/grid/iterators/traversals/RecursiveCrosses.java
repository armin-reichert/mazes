package de.amr.easy.grid.iterators.traversals;

import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.iterators.Sequence;

/**
 * Recursively fills a grid with crosses.
 * 
 * @author Armin Reichert
 */
public class RecursiveCrosses implements Sequence<Integer> {

	private final NakedGrid2D<?> grid;
	private final List<Integer> path = new ArrayList<>();

	public RecursiveCrosses(NakedGrid2D<?> grid) {
		this.grid = grid;
		cross(grid.numCols(), grid.numRows(), 0, 0);
	}

	@Override
	public Iterator<Integer> iterator() {
		return path.iterator();
	}

	private void cross(int width, int height, int startCol, int startRow) {
		if (width == 0 || height == 0) {
			return;
		}
		int w2 = width / 2, h2 = height / 2;
		int offsetX = width % 2, offsetY = height % 2;
		int centerCol = startCol + w2, centerRow = startRow + h2;

		// Vertical axis
		range(startRow, startRow + height).forEach(row -> {
			if (grid.isValidCol(centerCol) && grid.isValidRow(row)) {
				path.add(grid.cell(centerCol, row));
			}
		});

		// Horizontal axis
		range(startCol, startCol + width).forEach(col -> {
			if (grid.isValidCol(col) && grid.isValidRow(centerRow)) {
				path.add(grid.cell(col, centerRow));
			}
		});

		// Recursive calls
		cross(w2, h2, startCol, startRow);
		cross(w2, h2, centerCol + offsetX, centerRow + offsetY);
		cross(w2, h2, centerCol + offsetX, startRow);
		cross(w2, h2, startCol, centerRow + offsetY);
	}
}