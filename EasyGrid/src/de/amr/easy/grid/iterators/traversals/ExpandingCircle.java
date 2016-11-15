package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.Sequence;
import de.amr.easy.grid.iterators.shapes.Circle;

/**
 * A sequence of cells filling the grid like an expanding circle around the center.
 * 
 * @author Armin Reichert
 */
public class ExpandingCircle implements Sequence<Integer> {

	private final BareGrid2D<?> grid;
	private final Integer center;
	private final int minRadius;
	private final int maxRadius;
	private int expansionRate;

	public ExpandingCircle(BareGrid2D<?> grid, Integer center, int minRadius, int maxRadius) {
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
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private Circle circle = new Circle(grid, center, minRadius);
			private Iterator<Integer> iterator = circle.iterator();

			@Override
			public boolean hasNext() {
				if (iterator.hasNext()) {
					return true;
				}
				int newRadius = circle.getRadius() + expansionRate;
				if (newRadius > maxRadius) {
					return false;
				}
				circle = new Circle(grid, circle.getCenter(), newRadius);
				iterator = circle.iterator();
				return iterator.hasNext();
			}

			@Override
			public Integer next() {
				return iterator.next();
			}
		};
	}
}