package de.amr.easy.grid.impl;

import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * An observable grid with cell content.
 * 
 * @param <C>
 *          cell content data type
 * @param <W>
 *          passage weight type
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<C, W extends Comparable<W>> extends ObservableBareGrid<W>
		implements ObservableGrid2D<C, W> {

	private final VertexContent<C> content;

	/**
	 * Creates an observable grid with the given properties.
	 * 
	 * @param numCols
	 *          the number of columns ("width")
	 * @param numRows
	 *          the number of rows ("height")
	 * @param top
	 *          the topology
	 * @param defaultContent
	 *          the default vertex content
	 * @param sparse
	 *          if the vertex content will be sparse
	 */
	public ObservableGrid(int numCols, int numRows, Topology top, C defaultContent, boolean sparse) {
		super(numCols, numRows, top);
		content = sparse ? new SparseGridContent<>() : new DenseGridContent<>(numCols * numRows);
		content.setDefaultContent(defaultContent);
	}

	/**
	 * Creates an observable grid as a copy of the given grid. Observers are not copied.
	 * 
	 * @param grid
	 *          an observable grid
	 */
	public ObservableGrid(ObservableGrid<C, W> grid) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultContent(), grid.isSparse());
		vertexStream().forEach(v -> {
			C content = grid.get(v);
			if (!content.equals(grid.getDefaultContent())) {
				set(v, content);
			}
		});
	}

	// --- {@link VertexContent} interface ---

	@Override
	public void clearContent() {
		content.clearContent();
	}

	@Override
	public C getDefaultContent() {
		return content.getDefaultContent();
	}

	@Override
	public void setDefaultContent(C defaultContent) {
		content.setDefaultContent(defaultContent);
		fireGraphChange(this);
	}

	@Override
	public C get(int cell) {
		return content.get(cell);
	}

	@Override
	public void set(int cell, C newContent) {
		C oldContent = content.get(cell);
		content.set(cell, newContent);
		fireVertexChange(cell, oldContent, newContent);
	}

	@Override
	public boolean isSparse() {
		return content.isSparse();
	}
}
