package de.amr.easy.grid.impl;

import de.amr.easy.grid.api.DataGrid2D;
import de.amr.easy.grid.api.GridContent;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 * 
 * @param Content
 *          grid content type
 */
public class DataGrid<Content> extends Grid implements DataGrid2D<Content> {

	private GridContent<Content> gridContent;

	public DataGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefault(defaultContent);
	}

	public DataGrid(int numCols, int numRows, Content defaultContent) {
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
