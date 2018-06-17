package de.amr.easy.grid.impl;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Vertex;
import de.amr.easy.graph.impl.DenseSymbolTable;
import de.amr.easy.graph.impl.SparseSymbolTable;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * An observable 2D-grid graph with vertex objects.
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<V, E extends Edge> extends ObservableBareGrid<E> implements ObservableGrid2D<V, E> {

	private final Vertex<V> vertexTable;

	/**
	 * Creates an observable grid with the given properties.
	 * 
	 * @param numCols
	 *          the number of columns ("width")
	 * @param numRows
	 *          the number of rows ("height")
	 * @param top
	 *          the topology
	 * @param defaultVertex
	 *          the default vertex object
	 * @param sparse
	 *          if the vertex content will be sparse
	 */
	public ObservableGrid(int numCols, int numRows, Topology top, V defaultVertex, boolean sparse,
			BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		vertexTable = sparse ? new SparseSymbolTable<>() : new DenseSymbolTable<>(numCols * numRows);
		vertexTable.setDefaultVertex(defaultVertex);
	}

	/**
	 * Creates an observable grid as a copy of the given grid. Observers and edges are not copied.
	 * TODO: handle edges
	 * 
	 * @param grid
	 *          an observable grid
	 */
	public ObservableGrid(ObservableGrid<V, E> grid) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultVertex(), grid.isSparse(),
				grid.fnEdgeFactory);
		vertices().forEach(v -> {
			V vertex = grid.get(v);
			if (!vertex.equals(grid.getDefaultVertex())) {
				set(v, vertex);
			}
		});
	}

	// --- {@link Vertex} interface ---

	@Override
	public void clearVertexObjects() {
		vertexTable.clearVertexObjects();
	}

	@Override
	public V getDefaultVertex() {
		return vertexTable.getDefaultVertex();
	}

	@Override
	public void setDefaultVertex(V defaultVertex) {
		vertexTable.setDefaultVertex(defaultVertex);
		fireGraphChange(this);
	}

	@Override
	public V get(int v) {
		return vertexTable.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		V oldVertex = vertexTable.get(v);
		vertexTable.set(v, vertex);
		fireVertexChange(v, oldVertex, vertex);
	}

	@Override
	public boolean isSparse() {
		return vertexTable.isSparse();
	}
}