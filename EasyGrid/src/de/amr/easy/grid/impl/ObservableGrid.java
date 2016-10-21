package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * An observable grid with cell content.
 * 
 * @param <Content>
 *          cell content data type
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<Content> extends ObservableNakedGrid implements ObservableGrid2D<Content> {

	private VertexContent<Integer, Content> gridContent;

	public ObservableGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefault(defaultContent);
	}

	public ObservableGrid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GraphContent} interface ---

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
