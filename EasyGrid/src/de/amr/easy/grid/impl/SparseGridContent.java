package de.amr.easy.grid.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.VertexContent;

/**
 * Hash map storing the content for a grid.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class SparseGridContent<Content> implements VertexContent<Integer, Content> {

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
	public void clearContent() {
		gridContent.clear();
	}

	@Override
	public void setDefaultContent(Content cellContent) {
		defaultCellContent = cellContent;
	}
}