package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.Grid2D;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 * 
 * @param <Content>
 *          grid content type
 * @param <PassageWeight>
 *          passage weight type
 */
public class Grid<Content, PassageWeight extends Comparable<PassageWeight>> extends BareGrid<PassageWeight>
		implements Grid2D<Content, PassageWeight> {

	private VertexContent<Integer, Content> gridContent;

	public Grid(int numCols, int numRows, Content defaultContent, boolean sparse) {
		super(numCols, numRows);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefaultContent(defaultContent);
	}

	public Grid(int numCols, int numRows, Content defaultContent) {
		this(numCols, numRows, defaultContent, true);
	}

	// --- {@link GridContent} interface ---

	@Override
	public void clearContent() {
		gridContent.clearContent();
	}

	@Override
	public void setDefaultContent(Content cellContent) {
		gridContent.setDefaultContent(cellContent);
	}

	@Override
	public Content get(Integer cell) {
		return gridContent.get(cell);
	}

	@Override
	public void set(Integer cell, Content cellContent) {
		gridContent.set(cell, cellContent);
	}
}
