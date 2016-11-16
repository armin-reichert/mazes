package de.amr.easy.grid.impl;

import static java.util.Collections.shuffle;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.Topology;

/**
 * An implementation of the {@link BareGrid2D} interface.
 * 
 * @author Armin Reichert
 * 
 * @param <Weight>
 *          passage weight type
 */
public class BareGrid<Weight extends Comparable<Weight>> implements BareGrid2D<Weight> {

	private final Topology top;
	private final int colCount;
	private final int rowCount;
	private final int cellCount;
	private final BitSet bits;

	// helper methods

	private void checkCell(int cell) {
		if (cell < 0 || cell >= cellCount) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	private int index(int col, int row) {
		return col + row * colCount;
	}

	private int bit(int cell, int dir) {
		return cell * top.dirCount() + top.ord(dir);
	}

	private void connect(int p, int q, int dir, boolean connected) {
		bits.set(bit(p, dir), connected);
		bits.set(bit(q, top.inv(dir)), connected);
	}

	/**
	 * Creates a grid of size {@code nCols x nRows} with an empty edge set.
	 * 
	 * @param colCount
	 *          the number of columns of this grid
	 * @param rowCount
	 *          the number of rows of this grid
	 */
	public BareGrid(int colCount, int rowCount, Topology top) {
		if (colCount < 0) {
			throw new IllegalArgumentException("Illegal number of columns: " + colCount);
		}
		if (rowCount < 0) {
			throw new IllegalArgumentException("Illegal number of rows: " + rowCount);
		}
		this.colCount = colCount;
		this.rowCount = rowCount;
		this.cellCount = colCount * rowCount;
		this.top = top;
		this.bits = new BitSet(top.dirCount() * cellCount);
	}

	/**
	 * Creates a grid of size {@code nCols x nRows} with an empty edge set and 4-direction topology.
	 * 
	 * @param colCount
	 *          the number of columns of this grid
	 * @param rowCount
	 *          the number of rows of this grid
	 */
	public BareGrid(int colCount, int rowCount) {
		this(colCount, rowCount, new Top4());
	}

	// Implement {@link Graph} interface

	@Override
	public Stream<Integer> vertexStream() {
		return range(0, cellCount).boxed();
	}

	@Override
	public int vertexCount() {
		return cellCount;
	}

	@Override
	public Stream<WeightedEdge<Integer, Weight>> edgeStream() {
		Set<WeightedEdge<Integer, Weight>> edgeSet = new HashSet<>();
		vertexStream().forEach(cell -> {
			top.dirs().forEach(dir -> {
				if (isConnected(cell, dir)) {
					neighbor(cell, dir).ifPresent(neighbor -> {
						if (cell < neighbor) {
							edgeSet.add(new WeightedEdge<>(cell, neighbor));
						}
					});
				}
			});
		});
		return edgeSet.stream();
	}

	@Override
	public int edgeCount() {
		return bits.cardinality() / 2; // two bits used to store one edge
	}

	@Override
	public void addVertex(Integer p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<WeightedEdge<Integer, Weight>> edge(Integer p, Integer q) {
		checkCell(p);
		checkCell(q);
		return adjacent(p, q) ? Optional.of(new WeightedEdge<>(p, q)) : Optional.empty();
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		if (adjacent(p, q)) {
			throw new IllegalStateException("Cannot add edge (" + p + "," + q + ") twice");
		}
		if (!areNeighbors(p, q)) {
			throw new IllegalStateException("Cannot add edge between cells which are not neighbors");
		}
		direction(p, q).ifPresent(dir -> connect(p, q, dir, true));
	}

	@Override
	public void removeEdge(Integer p, Integer q) {
		if (!adjacent(p, q)) {
			throw new IllegalStateException("Cannot remove non-existing edge");
		}
		direction(p, q).ifPresent(dir -> connect(p, q, dir, false));
	}

	@Override
	public void removeEdges() {
		bits.clear();
	}

	@Override
	public Stream<Integer> adjVertices(Integer cell) {
		checkCell(cell);
		/*@formatter:off*/
		return top.dirs()
			.filter(dir -> isConnected(cell, dir))
			.map(dir -> neighbor(cell, dir).getAsInt())
			.boxed();
		/*@formatter:on*/
	}

	@Override
	public boolean adjacent(Integer either, Integer other) {
		checkCell(other);
		return adjVertices(either).anyMatch(vertex -> vertex.equals(other));
	}

	@Override
	public int degree(Integer cell) {
		return (int) adjVertices(cell).count();
	}

	// Implement {@link BareGrid2D} interface

	@Override
	public Topology getTopology() {
		return top;
	}

	@Override
	public int numCols() {
		return colCount;
	}

	@Override
	public int numRows() {
		return rowCount;
	}

	@Override
	public int cell(int col, int row) {
		if (!isValidCol(col)) {
			throw new IllegalArgumentException(String.format("Invalid col: (%d, %d)", col, row));
		}
		if (!isValidRow(row)) {
			throw new IllegalArgumentException(String.format("Invalid row: (%d, %d)", col, row));
		}
		return index(col, row);
	}

	@Override
	public int cell(GridPosition position) {
		switch (position) {
		case TOP_LEFT:
			return cell(0, 0);
		case TOP_RIGHT:
			return cell(colCount - 1, 0);
		case CENTER:
			return cell(colCount / 2, rowCount / 2);
		case BOTTOM_LEFT:
			return cell(0, rowCount - 1);
		case BOTTOM_RIGHT:
			return cell(colCount - 1, rowCount - 1);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int col(int cell) {
		checkCell(cell);
		return cell % colCount;
	}

	@Override
	public int row(int cell) {
		checkCell(cell);
		return cell / colCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	// TODO make this work for all topologies
	public BareGrid<Weight> makeFullGrid() {
		removeEdges();
		range(0, colCount).forEach(col -> {
			range(0, rowCount).forEach(row -> {
				if (col + 1 < colCount) {
					connect(index(col, row), index(col + 1, row), Top4.E, true); // TODO
				}
				if (row + 1 < rowCount) {
					connect(index(col, row), index(col, row + 1), Top4.S, true); // TODO
				}
			});
		});
		return this;
	}

	@Override
	// TODO make this work for all topologies
	public Stream<WeightedEdge<Integer, Weight>> fullGridEdgesPermuted() {
		List<WeightedEdge<Integer, Weight>> edges = new ArrayList<>();
		range(0, colCount).forEach(col -> {
			range(0, rowCount).forEach(row -> {
				if (col + 1 < colCount) {
					edges.add(new WeightedEdge<>(index(col, row), index(col + 1, row))); // TODO
				}
				if (row + 1 < rowCount) {
					edges.add(new WeightedEdge<>(index(col, row), index(col, row + 1))); // TODO
				}
			});
		});
		shuffle(edges);
		return edges.stream();
	}

	@Override
	public boolean isValidCol(int col) {
		return 0 <= col && col < colCount;
	}

	@Override
	public boolean isValidRow(int row) {
		return 0 <= row && row < rowCount;
	}

	@Override
	public boolean areNeighbors(int either, int other) {
		return neighbors(either).anyMatch(neighbor -> neighbor == other);
	}

	@Override
	public IntStream neighbors(int cell, IntStream dirs) {
		return dirs.filter(dir -> neighbor(cell, dir).isPresent()).map(dir -> neighbor(cell, dir).getAsInt());
	}

	@Override
	public IntStream neighbors(int cell) {
		return neighbors(cell, top.dirs());
	}

	@Override
	public IntStream neighborsPermuted(int cell) {
		return neighbors(cell, top.dirsPermuted());
	}

	@Override
	public OptionalInt neighbor(int cell, int dir) {
		checkCell(cell);
		int col = col(cell) + top.dx(dir);
		int row = row(cell) + top.dy(dir);
		if (isValidCol(col) && isValidRow(row)) {
			return OptionalInt.of(index(col, row));
		}
		return OptionalInt.empty();
	}

	@Override
	public boolean isConnected(int cell, int dir) {
		checkCell(cell);
		return bits.get(bit(cell, dir));
	}

	@Override
	public OptionalInt direction(int p, int q) {
		checkCell(p);
		checkCell(q);
		return top.dirs().filter(dir -> {
			OptionalInt neighbor = neighbor(p, dir);
			return neighbor.isPresent() && neighbor.getAsInt() == q;
		}).findFirst();
	}
}