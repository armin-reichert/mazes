package de.amr.easy.grid.impl;

import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridDataAccess;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 */
public class DataGrid<Content> extends RawGrid implements GridDataAccess<Integer, Content> {

	private GridDataAccess<Integer, Content> contentStore;

	public DataGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		contentStore = sparse ? new HashMapData<>() : new ArrayData<>(numCols * numRows);
		contentStore.setDefault(defaultContent);
	}

	public DataGrid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GridContentStore} interface ---

	@Override
	public void clear() {
		contentStore.clear();
	}

	@Override
	public void setDefault(Content content) {
		contentStore.setDefault(content);
	}

	@Override
	public Content get(Integer cell) {
		return contentStore.get(cell);
	}

	@Override
	public void set(Integer cell, Content content) {
		contentStore.set(cell, content);
	}
}
