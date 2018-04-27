package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.ObservableGrid2D;

/**
 * An observable grid with cell content.
 * 
 * @param <C>
 *          cell content data type
 * @param <W>
 *          passage weight type
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<C, W extends Comparable<W>> extends ObservableBareGrid<W>
		implements ObservableGrid2D<C, W> {

	private final VertexContent<C> gridContent;

	public ObservableGrid(int numCols, int numRows, C defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefaultContent(defaultContent);
	}

	public ObservableGrid(int numCols, int numRows, C defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link VertexContent} interface ---

	@Override
	public void clearContent() {
		gridContent.clearContent();
	}

	@Override
	public C getDefaultContent() {
		return gridContent.getDefaultContent();
	}

	@Override
	public void setDefaultContent(C content) {
		gridContent.setDefaultContent(content);
		fireGraphChange(this);
	}

	@Override
	public C get(int cell) {
		return gridContent.get(cell);
	}

	@Override
	public void set(int cell, C content) {
		C oldContent = gridContent.get(cell);
		gridContent.set(cell, content);
		fireVertexChange(cell, oldContent, content);
	}
}
