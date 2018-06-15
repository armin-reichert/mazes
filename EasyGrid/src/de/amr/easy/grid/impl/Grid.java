package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 * 
 * @param <C>
 *          vertex content type
 * @param <W>
 *          edge weight type
 */
public class Grid<C, W extends Comparable<W>> extends BareGrid<W> implements Grid2D<C, W> {

	private final VertexContent<C> content;

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
	public Grid(int numCols, int numRows, Topology top, C defaultContent, boolean sparse) {
		super(numCols, numRows, top);
		content = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		content.setDefaultContent(defaultContent);
	}

	/**
	 * Creates a copy of the given grid.
	 * 
	 * @param grid
	 *          the grid to copy
	 */
	public Grid(Grid2D<C, W> grid) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultContent(), grid.isSparse());
		vertices().forEach(v -> {
			C content = grid.get(v);
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
	public C getDefaultContent() {
		return content.getDefaultContent();
	}

	@Override
	public void setDefaultContent(C cellContent) {
		content.setDefaultContent(cellContent);
	}

	@Override
	public C get(int cell) {
		return content.get(cell);
	}

	@Override
	public void set(int cell, C cellContent) {
		content.set(cell, cellContent);
	}

	@Override
	public boolean isSparse() {
		return content.isSparse();
	}
}
