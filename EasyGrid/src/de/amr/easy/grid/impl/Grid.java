package de.amr.easy.grid.impl;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 */
public class Grid<V, E extends Edge> extends BareGrid<E> implements Grid2D<V, E> {

	private final VertexContent<V> content;

	/**
	 * Creates a grid with the given properties.
	 * 
	 * @param numCols
	 *          the number of columns ("width")
	 * @param numRows
	 *          the number of rows ("height")
	 * @param top
	 *          the topology of the grid
	 * @param defaultContent
	 *          the default content of grid cells
	 * @param sparse
	 *          if the grid has sparse content
	 */
	public Grid(int numCols, int numRows, Topology top, V defaultContent, boolean sparse,
			BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		content = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		content.setDefaultContent(defaultContent);
	}

	/**
	 * Creates a copy of the given grid.
	 * 
	 * @param grid
	 *          the grid to copy
	 */
	public Grid(Grid2D<V, E> grid, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultContent(), grid.isSparse(), fnEdgeFactory);
		vertices().forEach(v -> {
			V content = grid.get(v);
			if (!content.equals(grid.getDefaultContent())) {
				set(v, content);
			}
		});
	}

	// --- {@link VertexContent} interface ---

	@Override
	public void clearContent() {
		content.clearContent();
	}

	@Override
	public V getDefaultContent() {
		return content.getDefaultContent();
	}

	@Override
	public void setDefaultContent(V cellContent) {
		content.setDefaultContent(cellContent);
	}

	@Override
	public V get(int cell) {
		return content.get(cell);
	}

	@Override
	public void set(int cell, V cellContent) {
		content.set(cell, cellContent);
	}

	@Override
	public boolean isSparse() {
		return content.isSparse();
	}
}