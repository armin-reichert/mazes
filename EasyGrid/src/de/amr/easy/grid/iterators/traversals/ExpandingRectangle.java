package de.amr.easy.grid.iterators.traversals;

import java.util.Iterator;

import de.amr.easy.grid.api.Sequence;
import de.amr.easy.grid.iterators.shapes.Rectangle;

/**
 * A sequence of cells filling the grid like an expanding rectangle with left-upper corner at the
 * left-upper grid corner.
 * 
 * @author Armin Reichert
 */
public class ExpandingRectangle implements Sequence<Integer> {

	private final Rectangle startRectangle;
	private boolean expandHorizontally;
	private boolean expandVertically;
	private int maxExpansion;
	private int expansionRate;

	public ExpandingRectangle(Rectangle startRectangle) {
		this.startRectangle = startRectangle;
		this.expandHorizontally = true;
		this.expandVertically = true;
		this.maxExpansion = 0;
		this.expansionRate = 1;
	}

	public void setMaxExpansion(int maxExpansion) {
		this.maxExpansion = maxExpansion;
	}

	public void setExpandHorizontally(boolean expandHorizontally) {
		this.expandHorizontally = expandHorizontally;
	}

	public void setExpandVertically(boolean expandVertically) {
		this.expandVertically = expandVertically;
	}

	public void setExpansionRate(int expansionRate) {
		this.expansionRate = expansionRate;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private Rectangle currentRectangle = startRectangle;
			private Iterator<Integer> iterator = currentRectangle.iterator();
			private int expansion;

			@Override
			public boolean hasNext() {
				return expansion < maxExpansion || iterator.hasNext();
			}

			@Override
			public Integer next() {
				if (!iterator.hasNext()) {
					int width = currentRectangle.getWidth() + (expandHorizontally ? expansionRate : 0);
					int height = currentRectangle.getHeight() + (expandVertically ? expansionRate : 0);
					expansion += expansionRate;
					currentRectangle = new Rectangle(currentRectangle.grid, currentRectangle.getLeftUpperCorner(), width, height);
					iterator = currentRectangle.iterator();
				}
				return iterator.next();
			}
		};
	}
}
