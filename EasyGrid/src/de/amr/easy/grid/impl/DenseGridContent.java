package de.amr.easy.grid.impl;

import de.amr.easy.grid.api.GridContent;

/**
 * Array storing grid content.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class DenseGridContent<Content> implements GridContent<Content> {

	private Object[] cellContent;
	private Object defaultCellContent;

	public DenseGridContent(int size) {
		cellContent = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Content get(Integer cell) {
		Content content = (Content) cellContent[cell];
		return content != null ? content : (Content) defaultCellContent;
	}

	@Override
	public void set(Integer cell, Content content) {
		cellContent[cell] = content;
	}

	@Override
	public void clear() {
		cellContent = new Object[cellContent.length];
	}

	@Override
	public void setDefault(Content content) {
		defaultCellContent = content;
	}
}