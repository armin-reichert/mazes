package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;

/**
 * Array storing grid content.
 * 
 * @author Armin Reichert
 *
 * @param <C>
 *          cell content type
 */
class DenseGridContent<C> implements VertexContent<C> {

	private Object[] cellContent;
	private C defaultCellContent;

	public DenseGridContent(int size) {
		cellContent = new Object[size];
	}

	@SuppressWarnings("unchecked")
	@Override
	public C get(int cell) {
		C content = (C) cellContent[cell];
		return content != null ? content : (C) defaultCellContent;
	}

	@Override
	public void set(int cell, C content) {
		cellContent[cell] = content;
	}

	@Override
	public void clearContent() {
		cellContent = new Object[cellContent.length];
	}

	@Override
	public C getDefaultContent() {
		return defaultCellContent;
	}

	@Override
	public void setDefaultContent(C content) {
		defaultCellContent = content;
	}

	@Override
	public boolean isSparse() {
		return false;
	}
}