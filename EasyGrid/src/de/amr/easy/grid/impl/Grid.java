package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 * 
 * @param <Content>
 *          grid content type
 * @param <Weight>
 *          passage weight type
 */
public class Grid<Content, Weight extends Comparable<Weight>> extends BareGrid<Weight>
		implements Grid2D<Content, Weight> {

	private final VertexContent<Content> gridContent;

	public Grid(int numCols, int numRows, Topology top, Content defaultContent, boolean sparse) {
		super(numCols, numRows, top);
		gridContent = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		gridContent.setDefaultContent(defaultContent);
	}

	public Grid(int numCols, int numRows, Topology top, Content defaultContent) {
		this(numCols, numRows, top, defaultContent, true);
	}

	// --- {@link GridContent} interface ---

	@Override
	public void clearContent() {
		gridContent.clearContent();
	}

	@Override
	public Content getDefaultContent() {
		return gridContent.getDefaultContent();
	}

	@Override
	public void setDefaultContent(Content cellContent) {
		gridContent.setDefaultContent(cellContent);
	}

	@Override
	public Content get(int cell) {
		return gridContent.get(cell);
	}

	@Override
	public void set(int cell, Content cellContent) {
		gridContent.set(cell, cellContent);
	}
}
