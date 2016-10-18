package de.amr.easy.grid.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.GraphContent;

/**
 * Hash map storing the content for a grid.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class SparseGridContent<Content> implements GraphContent<Integer, Content> {

	private final Map<Integer, Content> gridContent = new HashMap<>();
	private Content defaultCellContent;

	@Override
	public Content get(Integer cell) {
		Content content = gridContent.get(cell);
		return content != null ? content : defaultCellContent;
	}

	@Override
	public void set(Integer cell, Content cellContent) {
		gridContent.put(cell, cellContent);
	}

	@Override
	public void clear() {
		gridContent.clear();
	}

	@Override
	public void setDefault(Content cellContent) {
		defaultCellContent = cellContent;
	}
}