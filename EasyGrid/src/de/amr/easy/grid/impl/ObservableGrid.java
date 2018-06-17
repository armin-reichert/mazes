package de.amr.easy.grid.impl;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Vertex;
import de.amr.easy.graph.impl.DenseSymbolTable;
import de.amr.easy.graph.impl.SparseSymbolTable;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * An observable grid with cell content.
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<V, E extends Edge> extends ObservableBareGrid<E> implements ObservableGrid2D<V, E> {

	private final Vertex<V> content;

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
	public ObservableGrid(int numCols, int numRows, Topology top, V defaultContent, boolean sparse,
			BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		content = sparse ? new SparseSymbolTable<>() : new DenseSymbolTable<>(numCols * numRows);
		content.setDefaultVertex(defaultContent);
	}

	/**
	 * Creates an observable grid as a copy of the given grid. Observers are not copied.
	 * 
	 * @param grid
	 *          an observable grid
	 */
	public ObservableGrid(ObservableGrid<V, E> grid, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultVertex(), grid.isSparse(), fnEdgeFactory);
		vertices().forEach(v -> {
			V content = grid.get(v);
			if (!content.equals(grid.getDefaultVertex())) {
				set(v, content);
			}
		});
	}

	// --- {@link VertexContent} interface ---

	@Override
	public void clearVertexObjects() {
		content.clearVertexObjects();
	}

	@Override
	public V getDefaultVertex() {
		return content.getDefaultVertex();
	}

	@Override
	public void setDefaultVertex(V defaultContent) {
		content.setDefaultVertex(defaultContent);
		fireGraphChange(this);
	}

	@Override
	public V get(int cell) {
		return content.get(cell);
	}

	@Override
	public void set(int cell, V newContent) {
		V oldContent = content.get(cell);
		content.set(cell, newContent);
		fireVertexChange(cell, oldContent, newContent);
	}

	@Override
	public boolean isSparse() {
		return content.isSparse();
	}
}