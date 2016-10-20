package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.GraphContent;
import de.amr.easy.grid.api.Grid2D;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 * 
 * @param Content
 *          grid content type
 */
public class Grid<Content> extends NakedGrid implements Grid2D<Content> {

	private GraphContent<Integer, Content> gridContent;

	public Grid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefault(defaultContent);
	}

	public Grid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GridContent} interface ---

	@Override
	public void clear() {
		gridContent.clear();
	}

	@Override
	public void setDefault(Content cellContent) {
		gridContent.setDefault(cellContent);
	}

	@Override
	public Content get(Integer cell) {
		return gridContent.get(cell);
	}

	@Override
	public void set(Integer cell, Content cellContent) {
		gridContent.set(cell, cellContent);
	}
}
