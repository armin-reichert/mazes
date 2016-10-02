package de.amr.easy.grid.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.grid.api.GridDataAccess;

/**
 * Hash map store for grid data.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          cell type
 * @param <Content>
 *          cell content type
 */
class HashMapData<Cell, Content> implements GridDataAccess<Cell, Content> {

	private final Map<Cell, Content> cellContent = new HashMap<>();
	private Content defaultContent;

	public HashMapData() {
	}

	@Override
	public Content get(Cell cell) {
		Content content = cellContent.get(cell);
		return content != null ? content : defaultContent;
	}

	@Override
	public void set(Cell cell, Content content) {
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
