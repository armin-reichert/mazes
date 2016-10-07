package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.shapes.Square;

/**
 * Traverses the grid like an expanding square.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public class ExpandingSquare<Cell> implements Sequence<Cell> {

	private final Grid2D<Cell, ?> grid;
	private final Cell topLeft;
	private final int minSize;
	private final int maxSize;

	public ExpandingSquare(Grid2D<Cell, ?> grid, Cell topLeft, int minSize, int maxSize) {
		this.grid = grid;
		this.topLeft = topLeft;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	@Override
	public Iterator<Cell> iterator() {

		return new Iterator<Cell>() {

			private Square<Cell> currentSquare = new Square<>(grid, topLeft, minSize);
			private Iterator<Cell> currentIterator = currentSquare.iterator();

			@Override
			public boolean hasNext() {
				return currentSquare.getSize() < maxSize || currentIterator.hasNext();
			}

			@Override
			public Cell next() {
				if (!currentIterator.hasNext()) {
					currentSquare = new Square<>(grid, topLeft, currentSquare.getSize() + 1);
					currentIterator = currentSquare.iterator();
				}
				return currentIterator.next();
			}
		};
	}
}
