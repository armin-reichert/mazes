package de.amr.easy.grid.iterators.shapes;

import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;

import java.util.Arrays;
import java.util.List;

import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.impl.Top4;

/**
 * Iterates grid cells clockwise as a square with given top left corner and size.
 * 
 * @author Armin Reichert
 *
 */
public class Square extends AbstractShape {

	private final Integer topLeft;
	private final int size;

	public Square(GridGraph2D<?> grid, Integer topLeft, int size) {
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

		List<Integer> dirs = Arrays.asList(E, S, W, N);
		for (int dir : dirs) {
			for (int i = 0; i < size - 1; ++i) {
				addCell(x, y);
				x += Top4.get().dx(dir);
				y += Top4.get().dy(dir);
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