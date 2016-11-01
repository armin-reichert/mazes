package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;

/**
 * Array storing grid content.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
class DenseGridContent<Content> implements VertexContent<Integer, Content> {

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
	public void clearContent() {
		cellContent = new Object[cellContent.length];
	}

	@Override
	public void setDefaultContent(Content content) {
		defaultCellContent = content;
	}
}