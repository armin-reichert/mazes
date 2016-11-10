package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.api.Sequence;
import de.amr.easy.grid.iterators.shapes.Square;

/**
 * A sequence of cells filling the grid like an expanding square.
 * 
 * @author Armin Reichert
 */
public class ExpandingSquare implements Sequence<Integer> {

	private final NakedGrid2D<Dir4,?> grid;
	private final Integer topLeft;
	private final int minSize;
	private final int maxSize;

	public ExpandingSquare(NakedGrid2D<Dir4,?> grid, Integer topLeft, int minSize, int maxSize) {
		this.grid = grid;
		this.topLeft = topLeft;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	@Override
	public Iterator<Integer> iterator() {

		return new Iterator<Integer>() {

			private Square currentSquare = new Square(grid, topLeft, minSize);
			private Iterator<Integer> currentIterator = currentSquare.iterator();

			@Override
			public boolean hasNext() {
				return currentSquare.getSize() < maxSize || currentIterator.hasNext();
			}

			@Override
			public Integer next() {
				if (!currentIterator.hasNext()) {
					currentSquare = new Square(grid, topLeft, currentSquare.getSize() + 1);
					currentIterator = currentSquare.iterator();
				}
				return currentIterator.next();
			}
		};
	}
}