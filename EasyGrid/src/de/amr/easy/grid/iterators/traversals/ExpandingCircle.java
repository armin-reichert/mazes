package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.shapes.Circle;

/**
 * Traverses the grid like an expanding circle.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          the grid cell type
 */
public class ExpandingCircle<Cell> implements Iterable<Cell> {

	private final Grid2D<Cell, ?> grid;
	private final Cell center;
	private final int minRadius;
	private final int maxRadius;
	private int expansionRate;

	public ExpandingCircle(Grid2D<Cell, ?> grid, Cell center, int minRadius, int maxRadius) {
		this.grid = grid;
		this.center = center;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		this.expansionRate = 1;
	}

	public void setExpansionRate(int expansionRate) {
		this.expansionRate = expansionRate;
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {

			private Circle<Cell> circle = new Circle<>(grid, center, minRadius);
			private Iterator<Cell> iterator = circle.iterator();

			@Override
			public boolean hasNext() {
				if (iterator.hasNext()) {
					return true;
				}
				int newRadius = circle.getRadius() + expansionRate;
				if (newRadius > maxRadius) {
					return false;
				}
				circle = new Circle<>(grid, circle.getCenter(), newRadius);
				iterator = circle.iterator();
				return iterator.hasNext();
			}

			@Override
			public Cell next() {
				return iterator.next();
			}
		};
	}
}