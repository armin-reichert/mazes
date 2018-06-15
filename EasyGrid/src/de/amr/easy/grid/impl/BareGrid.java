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
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.Topology;

/**
 * An implementation of the {@link BareGrid2D} interface.
 * 
 * @author Armin Reichert
 */
public class BareGrid<E extends Edge> implements BareGrid2D<E> {

	// TODO
	protected final BiFunction<Integer, Integer, E> fnEdgeFactory;

	protected Topology top;
	protected BitSet bits;
	protected final int colCount;
	protected final int rowCount;
	protected final int cellCount;

	// helper methods

	protected void checkCell(int cell) {
		if (cell < 0 || cell >= cellCount) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	protected void checkDir(int dir) {
		if (dir < 0 || dir > top.dirCount() - 1) {
			throw new IllegalArgumentException("Invalid direction: " + dir);
		}
	}

	protected int index(int col, int row) {
		return col + row * colCount;
	}

	protected int bit(int cell, int dir) {
		return cell * top.dirCount() + dir;
	}

	protected void connect(int p, int q, int dir, boolean connected) {
		bits.set(bit(p, dir), connected);
		bits.set(bit(q, top.inv(dir)), connected);
	}

	/**
	 * Creates a grid of size {@code colCount x rowCount} with an empty edge set.
	 * 
	 * @param colCount
	 *          the number of columns of this grid
	 * @param rowCount
	 *          the number of rows of this grid
	 * @param top
	 *          the topology of this grid
	 */
	public BareGrid(int colCount, int rowCount, Topology top, BiFunction<Integer, Integer, E> fnEdgeFactory) {
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
		this.fnEdgeFactory = fnEdgeFactory;
	}

	/**
	 * Creates a copy of the given grid.
	 * 
	 * @param grid
	 *          the grid to copy
	 */
	public BareGrid(BareGrid<E> grid, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this(grid.colCount, grid.rowCount, grid.top, fnEdgeFactory);
	}

	// Implement {@link Graph} interface

	@Override
	public IntStream vertices() {
		return range(0, cellCount);
	}

	@Override
	public int vertexCount() {
		return cellCount;
	}

	@Override
	public Stream<E> edges() {
		List<E> edgeList = new ArrayList<>();
		///*@formatter:off*/
		vertices().forEach(cell -> {
			top.dirs()
				.filter(dir -> isConnected(cell, dir))
				.mapToObj(dir -> neighbor(cell, dir))
				.filter(OptionalInt::isPresent)
				.map(OptionalInt::getAsInt)
				.filter(neighbor -> cell < neighbor)
				.forEach(neighbor -> edgeList.add(fnEdgeFactory.apply(cell, neighbor)));
		});
		///*@formatter:on*/
		return edgeList.stream();
	}

	@Override
	public int edgeCount() {
		return bits.cardinality() / 2; // two bits used to store one edge
	}

	@Override
	public void addVertex(int p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<E> edge(int p, int q) {
		checkCell(p);
		checkCell(q);
		return adjacent(p, q) ? Optional.of(fnEdgeFactory.apply(p, q)) : Optional.empty();
	}

	@Override
	public void addEdge(int p, int q) {
		if (!areNeighbors(p, q)) {
			throw new IllegalStateException("Cannot add edge between cells which are not neighbors");
		}
		if (adjacent(p, q)) {
			throw new IllegalStateException("Cannot add edge (" + p + "," + q + ") twice");
		}
		direction(p, q).ifPresent(dir -> connect(p, q, dir, true));
	}

	@Override
	public void removeEdge(int p, int q) {
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
	public IntStream adjVertices(int cell) {
		checkCell(cell);
		/*@formatter:off*/
		return top.dirs()
			.filter(dir -> isConnected(cell, dir))
			.map(dir -> neighbor(cell, dir).getAsInt());
		/*@formatter:on*/
	}

	@Override
	public boolean adjacent(int either, int other) {
		checkCell(other);
		return adjVertices(either).anyMatch(vertex -> vertex == other);
	}

	@Override
	public int degree(int cell) {
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

	@Override
	public void fill() {
		removeEdges();
		range(0, colCount).forEach(col -> {
			range(0, rowCount).forEach(row -> {
				int source = index(col, row);
				BitSet used = new BitSet(top.dirCount());
				top.dirs().forEach(dir -> {
					if (!used.get(dir)) {
						used.set(dir);
						used.set(top.inv(dir));
						int targetCol = col + top.dx(dir), targetRow = row + top.dy(dir);
						if (isValidCol(targetCol) && isValidRow(targetRow)) {
							connect(source, index(targetCol, targetRow), dir, true);
						}
					}
				});
			});
		});
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
		return dirs.mapToObj(dir -> neighbor(cell, dir)).filter(OptionalInt::isPresent).mapToInt(OptionalInt::getAsInt);
	}

	@Override
	public IntStream neighbors(int cell) {
		return neighbors(cell, top.dirs());
	}

	@Override
	public OptionalInt neighbor(int cell, int dir) {
		checkCell(cell);
		checkDir(dir);
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
		checkDir(dir);
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