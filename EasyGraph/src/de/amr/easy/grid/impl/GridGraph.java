package de.amr.easy.grid.impl;

import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.EdgeLabels;
import de.amr.easy.graph.api.VertexLabels;
import de.amr.easy.graph.impl.EdgeLabelsMap;
import de.amr.easy.graph.impl.VertexLabelsMap;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.Topology;

/**
 * An implementation of the {@link GridGraph2D} interface.
 * 
 * @author Armin Reichert
 * 
 * @param <V>
 *          vertex label type
 * @param <E>
 *          edge label type
 */
public class GridGraph<V, E> implements GridGraph2D<V, E> {

	protected final int numCols;
	protected final int numRows;
	protected final int numCells;
	protected final BiFunction<Integer, Integer, Edge<E>> fnEdgeFactory;
	protected Topology top;
	protected BitSet bits;

	// {@link VertexLabels}

	private final VertexLabels<V> vertexLabels;

	@Override
	public void clearVertexLabels() {
		vertexLabels.clearVertexLabels();
	}

	@Override
	public V getDefaultVertexLabel() {
		return vertexLabels.getDefaultVertexLabel();
	}

	@Override
	public void setDefaultVertexLabel(V vertex) {
		vertexLabels.setDefaultVertexLabel(vertex);
	}

	@Override
	public V get(int v) {
		return vertexLabels.get(v);
	}

	@Override
	public void set(int v, V vertex) {
		vertexLabels.set(v, vertex);
	}

	// {@link EdgeLabels}

	private final EdgeLabels<E> edgeLabels;

	@Override
	public void clearEdgeLabels() {
		edgeLabels.clearEdgeLabels();
	}

	@Override
	public E getDefaultEdgeLabel(int u, int v) {
		return edgeLabels.getDefaultEdgeLabel(u, v);
	}

	@Override
	public void setDefaultEdgeLabel(BiFunction<Integer, Integer, E> fnDefaultLabel) {
		edgeLabels.setDefaultEdgeLabel(fnDefaultLabel);
	}

	@Override
	public E getEdgeLabel(int u, int v) {
		return edgeLabels.getEdgeLabel(u, v);
	}

	@Override
	public void setEdgeLabel(int u, int v, E e) {
		edgeLabels.setEdgeLabel(u, v, e);
	}

	// helper methods

	protected void checkCell(int cell) {
		if (cell < 0 || cell >= numCells) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	protected void checkDir(int dir) {
		if (dir < 0 || dir >= top.dirCount()) {
			throw new IllegalArgumentException("Invalid direction: " + dir);
		}
	}

	protected int index(int col, int row) {
		return row * numCols + col;
	}

	protected int bit(int cell, int dir) {
		return cell * top.dirCount() + dir;
	}

	protected void connect(int u, int v, int dir, boolean connected) {
		bits.set(bit(u, dir), connected);
		bits.set(bit(v, top.inv(dir)), connected);
	}

	/**
	 * Creates a grid with the given properties.
	 * 
	 * @param numCols
	 *          the number of columns
	 * @param numRows
	 *          the number of rows
	 * @param top
	 *          the topology of this grid
	 * @param defaultVertexLabel
	 *          default vertex label
	 * @param defaultEdgeLabel
	 *          default edge label
	 * @param fnEdgeFactory
	 *          function for creating edges of the correct type
	 */
	public GridGraph(int numCols, int numRows, Topology top, V defaultVertexLabel,
			BiFunction<Integer, Integer, E> fnDefaultEdgeLabel, BiFunction<Integer, Integer, Edge<E>> fnEdgeFactory) {
		if (numCols < 0) {
			throw new IllegalArgumentException("Illegal number of columns: " + numCols);
		}
		if (numRows < 0) {
			throw new IllegalArgumentException("Illegal number of rows: " + numRows);
		}
		if (top == null) {
			throw new IllegalArgumentException("Grid topology must be specified");
		}
		if (fnEdgeFactory == null) {
			throw new IllegalArgumentException("Edge factory must be specified");
		}
		this.numCols = numCols;
		this.numRows = numRows;
		this.numCells = numCols * numRows;
		this.top = top;
		this.bits = new BitSet(top.dirCount() * numCells);
		this.fnEdgeFactory = fnEdgeFactory;

		vertexLabels = new VertexLabelsMap<>(defaultVertexLabel);
		edgeLabels = new EdgeLabelsMap<>(fnDefaultEdgeLabel);
	}

	// Implement {@link Graph} interface

	@Override
	public IntStream vertices() {
		return range(0, numCells);
	}

	@Override
	public int numVertices() {
		return numCells;
	}

	@Override
	public Stream<Edge<E>> edges() {
		List<Edge<E>> edgeList = new ArrayList<>();
		/*@formatter:off*/
		vertices().forEach(cell -> {
			top.dirs()
				.filter(dir -> isConnected(cell, dir))
				.mapToObj(dir -> neighbor(cell, dir))
				.filter(OptionalInt::isPresent)
				.map(OptionalInt::getAsInt)
				.filter(neighbor -> cell < neighbor)
				.forEach(neighbor -> edgeList.add(fnEdgeFactory.apply(cell, neighbor)));
		});
		/*@formatter:on*/
		return edgeList.stream();
	}

	@Override
	public int numEdges() {
		return bits.cardinality() / 2; // two bits are used to store one edge
	}

	@Override
	public void addVertex(int v) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeVertex(int v) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<Edge<E>> edge(int u, int v) {
		checkCell(u);
		checkCell(v);
		return hasEdge(u, v) ? Optional.of(fnEdgeFactory.apply(u, v)) : Optional.empty();
	}

	@Override
	public void addEdge(int u, int v) {
		if (!areNeighbors(u, v)) {
			throw new IllegalStateException("Cannot add edge between cells which are not neighbors");
		}
		if (hasEdge(u, v)) {
			throw new IllegalStateException(String.format("Cannot add edge {%s,%s} twice", u, v));
		}
		direction(u, v).ifPresent(dir -> connect(u, v, dir, true));
	}

	@Override
	public void addEdge(int u, int v, E e) {
		addEdge(u, v);
		setEdgeLabel(u, v, e);
	}

	@Override
	public void removeEdge(int u, int v) {
		if (!hasEdge(u, v)) {
			throw new IllegalStateException(String.format("Cannot remove non-existing edge {%s,%s}", u, v));
		}
		direction(u, v).ifPresent(dir -> connect(u, v, dir, false));
	}

	@Override
	public void removeEdges() {
		bits.clear();
	}

	@Override
	public IntStream adj(int v) {
		checkCell(v);
		return top.dirs().filter(dir -> isConnected(v, dir)).map(dir -> neighbor(v, dir).getAsInt());
	}

	@Override
	public boolean hasEdge(int u, int v) {
		checkCell(v);
		return adj(u).anyMatch(x -> x == v);
	}

	@Override
	public int degree(int v) {
		return (int) adj(v).count();
	}

	// Implement {@link BareGridGraph2D} interface

	@Override
	public Topology getTopology() {
		return top;
	}

	@Override
	public int numCols() {
		return numCols;
	}

	@Override
	public int numRows() {
		return numRows;
	}

	@Override
	public int cell(int col, int row) {
		if (!isValidCol(col)) {
			throw new IllegalArgumentException(String.format("Invalid col: %d", col));
		}
		if (!isValidRow(row)) {
			throw new IllegalArgumentException(String.format("Invalid row: %d", row));
		}
		return index(col, row);
	}

	@Override
	public int cell(GridPosition position) {
		switch (position) {
		case TOP_LEFT:
			return cell(0, 0);
		case TOP_RIGHT:
			return cell(numCols - 1, 0);
		case CENTER:
			return cell(numCols / 2, numRows / 2);
		case BOTTOM_LEFT:
			return cell(0, numRows - 1);
		case BOTTOM_RIGHT:
			return cell(numCols - 1, numRows - 1);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int col(int cell) {
		checkCell(cell);
		return cell % numCols;
	}

	@Override
	public int row(int cell) {
		checkCell(cell);
		return cell / numCols;
	}

	@Override
	public void fill() {
		bits.clear();
		range(0, numCols).forEach(col -> {
			range(0, numRows).forEach(row -> {
				int u = index(col, row);
				BitSet used = new BitSet(top.dirCount());
				top.dirs().forEach(dir -> {
					if (!used.get(dir)) {
						used.set(dir);
						used.set(top.inv(dir));
						int col_v = col + top.dx(dir), row_v = row + top.dy(dir);
						if (isValidCol(col_v) && isValidRow(row_v)) {
							int v = index(col_v, row_v);
							connect(u, v, dir, true);
						}
					}
				});
			});
		});
	}

	@Override
	public boolean isValidCol(int col) {
		return 0 <= col && col < numCols;
	}

	@Override
	public boolean isValidRow(int row) {
		return 0 <= row && row < numRows;
	}

	@Override
	public boolean areNeighbors(int u, int v) {
		return neighbors(u).anyMatch(x -> x == v);
	}

	@Override
	public IntStream neighbors(int v, IntStream dirs) {
		return dirs.mapToObj(dir -> neighbor(v, dir)).filter(OptionalInt::isPresent).mapToInt(OptionalInt::getAsInt);
	}

	@Override
	public IntStream neighbors(int v) {
		return neighbors(v, top.dirs());
	}

	@Override
	public OptionalInt neighbor(int v, int dir) {
		checkCell(v);
		checkDir(dir);
		int col = col(v) + top.dx(dir);
		int row = row(v) + top.dy(dir);
		return isValidCol(col) && isValidRow(row) ? OptionalInt.of(index(col, row)) : OptionalInt.empty();
	}

	@Override
	public boolean isConnected(int v, int dir) {
		checkCell(v);
		checkDir(dir);
		return bits.get(bit(v, dir));
	}

	@Override
	public OptionalInt direction(int u, int v) {
		checkCell(u);
		checkCell(v);
		return top.dirs().filter(dir -> {
			OptionalInt neighbor = neighbor(u, dir);
			return neighbor.isPresent() && neighbor.getAsInt() == v;
		}).findFirst();
	}
}