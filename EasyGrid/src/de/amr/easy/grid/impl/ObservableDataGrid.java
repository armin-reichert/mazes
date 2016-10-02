package de.amr.easy.grid.impl;

import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.GridDataAccess;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * An observable grid with cell content.
 * 
 * @param <Content>
 *          cell content data type
 * 
 * @author Armin Reichert
 */
public class ObservableDataGrid<Content> extends ObservableRawGrid
		implements ObservableDataGrid2D<Integer, DefaultEdge<Integer>, Content> {

	private GridDataAccess<Integer, Content> data;

	public ObservableDataGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		data = sparse ? new HashMapData<>() : new ArrayData<>(numCols * numRows);
		data.setDefault(defaultContent);
	}

	public ObservableDataGrid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GridDataAccess} interface ---

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public void setDefault(Content content) {
		data.setDefault(content);
	}

	@Override
	public Content get(Integer cell) {
		return data.get(cell);
	}

	@Override
	public void set(Integer cell, Content content) {
		Content oldContent = data.get(cell);
		data.set(cell, content);
		fireVertexChange(cell, oldContent, content);
	}
}
