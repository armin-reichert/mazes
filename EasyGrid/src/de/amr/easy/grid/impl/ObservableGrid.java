package de.amr.easy.grid.impl;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.VertexMap;
import de.amr.easy.graph.impl.DenseVertexMap;
import de.amr.easy.graph.impl.SparseVertexMap;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * An observable 2D-grid graph with vertex objects.
 * 
 * @author Armin Reichert
 */
public class ObservableGrid<V, E extends Edge> extends ObservableGridGraph<E> implements ObservableGrid2D<V, E> {

	private final VertexMap<V> vertexMap;

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
	 *          if the grid is sparse of vertex objects
	 */
	public ObservableGrid(int numCols, int numRows, Topology top, V defaultVertex, boolean sparse,
			BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		vertexMap = sparse ? new SparseVertexMap<>() : new DenseVertexMap<>(numCols * numRows);
		vertexMap.setDefaultVertex(defaultVertex);
	}

	// --- {@link VertexMap} interface ---

	@Override
	public void clear() {
		vertexMap.clear();
		fireGraphChange(this);
	}

	@Override
	public V getDefaultVertex() {
		return vertexMap.getDefaultVertex();
	}

	@Override
	public void setDefaultVertex(V defaultVertex) {
		vertexMap.setDefaultVertex(defaultVertex);
		fireGraphChange(this);
	}

	@Override
	public V get(int v) {
		return vertexMap.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		V oldVertex = vertexMap.get(v);
		vertexMap.set(v, vertex);
		fireVertexChange(v, oldVertex, vertex);
	}
}