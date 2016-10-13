package de.amr.easy.grid.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.grid.api.GridDataAccess;

/**
 * Hash map store for grid data.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class HashMapGridDataAccess<Content> implements GridDataAccess<Content> {

	private final Map<Integer, Content> cellContent = new HashMap<>();
	private Content defaultContent;

	public HashMapGridDataAccess() {
	}

	@Override
	public Content get(Integer cell) {
		Content content = cellContent.get(cell);
		return content != null ? content : defaultContent;
	}

	@Override
	public void set(Integer cell, Content content) {
		cellContent.put(cell, content);
	}

	@Override
	public void clear() {
		cellContent.clear();
	}

	@Override
	public void setDefault(Content content) {
		defaultContent = content;
	}
}
