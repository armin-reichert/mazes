package de.amr.easy.grid.impl;

import de.amr.easy.grid.api.GridDataAccess;

/**
 * Array storing grid content.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class ArrayGridDataAccess<Content> implements GridDataAccess<Content> {

	private Object[] cellContent;
	private Object defaultContent;

	public ArrayGridDataAccess(int size) {
		cellContent = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public Content get(Integer cell) {
		Content content = (Content) cellContent[cell];
		return content != null ? content : (Content) defaultContent;
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
		defaultContent = content;
	}
}