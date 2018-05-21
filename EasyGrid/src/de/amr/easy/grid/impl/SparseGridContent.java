package de.amr.easy.grid.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.VertexContent;

/**
 * Hash map storing the content for a grid.
 * 
 * @author Armin Reichert
 *
 * @param <C>
 *          cell content type
 */
class SparseGridContent<C> implements VertexContent<C> {

	private final Map<Integer, C> gridContent = new HashMap<>();
	private C defaultCellContent;

	@Override
	public C get(int cell) {
		C content = gridContent.get(cell);
		return content != null ? content : defaultCellContent;
	}

	@Override
	public void set(int cell, C cellContent) {
		gridContent.put(cell, cellContent);
	}

	@Override
	public void clearContent() {
		gridContent.clear();
	}

	@Override
	public C getDefaultContent() {
		return defaultCellContent;
	}

	@Override
	public void setDefaultContent(C cellContent) {
		defaultCellContent = cellContent;
	}

	@Override
	public boolean isSparse() {
		return true;
	}
}