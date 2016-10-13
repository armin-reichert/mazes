package de.amr.easy.grid.iterators.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.iterators.IteratorFactory;

public class A<C> extends Shape<C> implements Iterable<C> {

	private final List<Iterator<C>> facets = new ArrayList<>();

	public A(Grid2D<C> grid, C leftUpperCorner, int width, int height, int thickness) {
		super(grid);
		int x = grid.col(leftUpperCorner), y = grid.row(leftUpperCorner);
		facets.add(new Rectangle<C>(grid, leftUpperCorner, thickness, height).iterator());
		facets.add(new Rectangle<C>(grid, leftUpperCorner, width, thickness).iterator());
		facets.add(new Rectangle<C>(grid, grid.cell(x, y + height / 3), width, thickness).iterator());
		facets.add(new Rectangle<C>(grid, grid.cell(x + width - thickness, y), 1, height).iterator());
	}

	@Override
	public Iterator<C> iterator() {
		return IteratorFactory.seq(facets);
	}
}
