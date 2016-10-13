package de.amr.easy.grid.iterators.shapes;

import de.amr.easy.grid.api.Grid2D;

/**
 * A rectangle with given cell as left upper corner.
 * 
 * @author Armin Reichert
 *
 * @param <Integer>
 *          grid cell type
 */
public class Rectangle extends Shape {

	private final Integer leftUpperCorner;
	private final int width;
	private final int height;

	public Rectangle(Grid2D grid, Integer leftUpperCorner, int width, int height) {
		super(grid);

		this.leftUpperCorner = leftUpperCorner;
		this.width = width;
		this.height = height;

		int cornerX = grid.col(leftUpperCorner), cornerY = grid.row(leftUpperCorner);
		int x = cornerX - 1, y = cornerY;
		for (int i = 0; i < width; ++i) {
			++x;
			addCell(x, y);
		}
		for (int i = 1; i < height; ++i) {
			++y;
			addCell(x, y);
		}
		for (int i = 1; i < width; ++i) {
			--x;
			addCell(x, y);
		}
		for (int i = 1; i < height - 1; ++i) {
			--y;
			addCell(x, y);
		}
	}

	public Integer getLeftUpperCorner() {
		return leftUpperCorner;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
