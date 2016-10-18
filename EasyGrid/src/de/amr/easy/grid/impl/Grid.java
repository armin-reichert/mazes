package de.amr.easy.grid.impl;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.S;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;

/**
 * Implementation of the {@link Grid2D} interface.
 * 
 * @author Armin Reichert
 */
public class Grid implements Grid2D {

	private static final int DIRECTION_COUNT = Direction.values().length;

	private final int numCols;
	private final int numRows;
	private final int numCells;
	private final BitSet connections;

	private void checkCell(Integer cell) {
		if (cell == null || cell < 0 || cell >= numCells) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	private int index(int col, int row) {
		return col + row * numCols;
	}

	private int bit(int cell, Direction dir) {
		return cell * DIRECTION_COUNT + dir.ordinal();
	}

	private void connect(int p, int q, Direction dir, boolean connected) {
		connections.set(bit(p, dir), connected);
		connections.set(bit(q, dir.inverse()), connected);
	}

	public Grid(int numCols, int numRows) {
		if (numCols < 0) {
			throw new IllegalArgumentException("Invalid number of columns: " + numCols);
		}
		if (numRows < 0) {
			throw new IllegalArgumentException("Invalid number of rows: " + numRows);
		}
		this.numCols = numCols;
		this.numRows = numRows;
		this.numCells = numCols * numRows;
		connections = new BitSet(DIRECTION_COUNT * numCells);
	}

	@Override
	public Stream<Integer> vertexStream() {
		return IntStream.range(0, numCells).boxed();
	}

	@Override
	public int vertexCount() {
		return numCells;
	}

	@Override
	public void makeFullGrid() {
		removeEdges();
		IntStream.range(0, numCols).forEach(col -> {
			IntStream.range(0, numRows).forEach(row -> {
				if (col + 1 < numCols) {
					connect(index(col, row), index(col + 1, row), E, true);
				}
				if (row + 1 < numRows) {
					connect(index(col, row), index(col, row + 1), S, true);
				}
			});
		});
	}

	@Override
	public Stream<WeightedEdge<Integer>> edgeStream() {
		Set<WeightedEdge<Integer>> edgeSet = new HashSet<>();
		vertexStream().forEach(cell -> {
			Stream.of(Direction.values()).forEach(dir -> {
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
		return connections.cardinality() / 2; // two bits are set for each edge
	}

	@Override
	public void addVertex(Integer p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<WeightedEdge<Integer>> edge(Integer p, Integer q) {
		checkCell(p);
		checkCell(q);
		return adjacent(p, q) ? Optional.of(new WeightedEdge<>(p, q)) : Optional.empty();
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		if (adjacent(p, q)) {
			throw new IllegalStateException("Cannot add edge twice");
		}
		if (neighbors(p).noneMatch(neighbor -> neighbor.equals(q))) {
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
		connections.clear();
	}

	@Override
	public Stream<Integer> adjVertices(Integer cell) {
		checkCell(cell);
		/*@formatter:off*/
		return Stream.of(Direction.values())
				.filter(dir -> isConnected(cell, dir))
				.map(dir -> neighbor(cell, dir).get());
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

	@Override
	public int numCols() {
		return numCols;
	}

	@Override
	public int numRows() {
		return numRows;
	}

	@Override
	public Integer cell(int col, int row) {
		if (!isValidCol(col)) {
			throw new IllegalArgumentException(String.format("Invalid col: (%d, %d)", col, row));
		}
		if (!isValidRow(row)) {
			throw new IllegalArgumentException(String.format("Invalid row: (%d, %d)", col, row));
		}
		return index(col, row);
	}

	@Override
	public Integer cell(GridPosition position) {
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
	public int col(Integer cell) {
		checkCell(cell);
		return cell % numCols;
	}

	@Override
	public int row(Integer cell) {
		checkCell(cell);
		return cell / numCols;
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
	public Stream<Integer> neighbors(Integer cell, Direction... dirs) {
		if (dirs.length == 0) {
			dirs = Direction.values();
		}
		return Stream.of(dirs).map(dir -> neighbor(cell, dir)).filter(Optional::isPresent).map(Optional::get);
	}

	@Override
	public Optional<Integer> neighbor(Integer cell, Direction dir) {
		checkCell(cell);
		int col = col(cell) + dir.dx;
		int row = row(cell) + dir.dy;
		if (isValidCol(col) && isValidRow(row)) {
			return Optional.of(index(col, row));
		}
		return Optional.empty();
	}

	@Override
	public boolean isConnected(Integer cell, Direction dir) {
		checkCell(cell);
		return connections.get(bit(cell, dir));
	}

	@Override
	public Optional<Direction> direction(Integer source, Integer target) {
		checkCell(source);
		checkCell(target);
		/*@formatter:off*/
		return Stream.of(Direction.values())
			.filter(dir -> neighbor(source, dir).isPresent())
			.filter(dir -> target.equals(neighbor(source, dir).get()))
			.findFirst();
		/*@formatter:on*/
	}
}