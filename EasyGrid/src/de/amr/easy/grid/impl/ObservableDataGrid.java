package de.amr.easy.grid.impl;

import de.amr.easy.grid.api.GridContent;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * An observable grid with cell content.
 * 
 * @param <Content>
 *          cell content data type
 * 
 * @author Armin Reichert
 */
public class ObservableDataGrid<Content> extends ObservableGrid implements ObservableDataGrid2D<Content> {

	private GridContent<Content> gridContent;

	public ObservableDataGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefault(defaultContent);
	}

	public ObservableDataGrid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GridDataAccess} interface ---

	@Override
	public void clear() {
		gridContent.clear();
	}

	@Override
	public void setDefault(Content content) {
		gridContent.setDefault(content);
	}

	@Override
	public Content get(Integer cell) {
		return gridContent.get(cell);
	}

	@Override
	public void set(Integer cell, Content content) {
		Content oldContent = gridContent.get(cell);
		gridContent.set(cell, content);
		fireVertexChange(cell, oldContent, content);
	}
}
