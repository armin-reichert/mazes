package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.api.dir.Dir4;

/**
 * An observable grid with cell content.
 * 
 * @param <Content>
 *          cell content data type
 * 
 * @param <PassageWeight>
 *          passage weight type
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<Content, PassageWeight extends Comparable<PassageWeight>>
		extends ObservableNakedGrid<PassageWeight> implements ObservableGrid2D<Dir4, Content, PassageWeight> {

	private VertexContent<Integer, Content> gridContent;

	public ObservableGrid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefaultContent(defaultContent);
	}

	public ObservableGrid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GraphContent} interface ---

	@Override
	public void clearContent() {
		gridContent.clearContent();
	}

	@Override
	public void setDefaultContent(Content content) {
		gridContent.setDefaultContent(content);
		fireGraphChange(this);
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
