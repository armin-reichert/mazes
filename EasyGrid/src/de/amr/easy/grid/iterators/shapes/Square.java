package de.amr.easy.grid.iterators.shapes;

import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;

/**
 * Iterates grid cells clockwise as a square with given top left corner and size.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          grid cell type
 */
public class Square<Cell> extends Shape<Cell> {

	private final Cell topLeft;
	private final int size;

	public Square(Grid2D<Cell, ?> grid, Cell topLeft, int size) {
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
		Direction[] clockwise = { Direction.E, Direction.S, Direction.W, Direction.N };
		for (Direction dir : clockwise) {
			for (int i = 0; i < size - 1; ++i) {
				addCell(x, y);
				x += dir.dx;
				y += dir.dy;
			}
		}
	}

	public Cell getTopLeft() {
		return topLeft;
	}

	public int getSize() {
		return size;
	}
}
