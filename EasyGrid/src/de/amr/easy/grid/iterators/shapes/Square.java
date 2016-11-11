package de.amr.easy.grid.iterators.shapes;

import static de.amr.easy.grid.api.dir.Dir4.E;
import static de.amr.easy.grid.api.dir.Dir4.N;
import static de.amr.easy.grid.api.dir.Dir4.S;
import static de.amr.easy.grid.api.dir.Dir4.W;

import java.util.Arrays;

import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.api.dir.Dir4;

/**
 * Iterates grid cells clockwise as a square with given top left corner and size.
 * 
 * @author Armin Reichert
 *
 */
public class Square extends Shape {

	private final Integer topLeft;
	private final int size;

	public Square(NakedGrid2D<?> grid, Integer topLeft, int size) {
		super(grid);

		this.topLeft = topLeft;
		this.size = size;
		if (size == 0) {
			return;
		}

		int x = grid.col(topLeft), y = grid.row(topLeft);
		if (size == 1) {
			addCell(x, y);
			return;
		}

		for (Dir4 dir : Arrays.asList(E, S, W, N)) {
			for (int i = 0; i < size - 1; ++i) {
				addCell(x, y);
				x += dir.dx();
				y += dir.dy();
			}
		}
	}

	public Integer getTopLeft() {
		return topLeft;
	}

	public int getSize() {
		return size;
	}
}