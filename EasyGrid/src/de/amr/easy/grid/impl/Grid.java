package de.amr.easy.grid.impl;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Vertex;
import de.amr.easy.graph.impl.DenseSymbolTable;
import de.amr.easy.graph.impl.SparseSymbolTable;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid with cell content.
 * 
 * @author Armin Reichert
 */
public class Grid<V, E extends Edge> extends BareGrid<E> implements Grid2D<V, E> {

	private final Vertex<V> symbolTable;

	/**
	 * Creates a grid with the given properties.
	 * 
	 * @param numCols
	 *          the number of columns ("width")
	 * @param numRows
	 *          the number of rows ("height")
	 * @param top
	 *          the topology of the grid
	 * @param defaultContent
	 *          the default content of grid cells
	 * @param sparse
	 *          if the grid has sparse content
	 */
	public Grid(int numCols, int numRows, Topology top, V defaultContent, boolean sparse,
			BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		symbolTable = sparse ? new SparseSymbolTable<>() : new DenseSymbolTable<>(numCols * numRows);
		symbolTable.setDefaultVertex(defaultContent);
	}

	/**
	 * Creates a copy of the given grid.
	 * 
	 * @param grid
	 *          the grid to copy
	 */
	public Grid(Grid2D<V, E> grid, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), grid.getDefaultVertex(), grid.isSparse(), fnEdgeFactory);
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
		symbolTable.clearVertexObjects();
	}

	@Override
	public V getDefaultVertex() {
		return symbolTable.getDefaultVertex();
	}

	@Override
	public void setDefaultVertex(V vertex) {
		symbolTable.setDefaultVertex(vertex);
	}

	@Override
	public V get(int v) {
		return symbolTable.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		symbolTable.set(v, vertex);
	}

	@Override
	public boolean isSparse() {
		return symbolTable.isSparse();
	}
}