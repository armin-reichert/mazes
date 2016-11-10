package de.amr.easy.grid.impl;

import static de.amr.easy.grid.api.Dir4.E;
import static de.amr.easy.grid.api.Dir4.S;
import static java.util.Collections.shuffle;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.NakedGrid2D;

/**
 * An implementation of the {@link NakedGrid2D} interface.
 * 
 * @author Armin Reichert
 * 
 * @param <PassageWeight>
 *          passage weight type
 */
public class NakedGrid<PassageWeight extends Comparable<PassageWeight>> implements NakedGrid2D<Dir4, PassageWeight> {

	private static final int DIRECTION_COUNT = Dir4.values().length;

	private final int nCols;
	private final int nRows;
	private final int nCells;
	private final BitSet connections;

	// helper methods

	private void checkCell(Integer cell) {
		if (cell == null || cell < 0 || cell >= nCells) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	private int index(int col, int row) {
		return col + row * nCols;
	}

	private int bit(int cell, Dir4 dir) {
		return cell * DIRECTION_COUNT + dir.ordinal();
	}

	private void connect(int p, int q, Dir4 dir, boolean connected) {
		connections.set(bit(p, dir), connected);
		connections.set(bit(q, dir.inverse()), connected);
	}

	/**
	 * Creates a grid of size {@code nCols x nRows} with an empty edge set.
	 * 
	 * @param nCols
	 *          the number of columns of this grid
	 * @param nRows
	 *          the number of rows of this grid
	 */
	public NakedGrid(int nCols, int nRows) {
		if (nCols < 0) {
			throw new IllegalArgumentException("Illegal number of columns: " + nCols);
		}
		if (nRows < 0) {
			throw new IllegalArgumentException("Illegal number of rows: " + nRows);
		}
		this.nCols = nCols;
		this.nRows = nRows;
		this.nCells = nCols * nRows;
		connections = new BitSet(DIRECTION_COUNT * nCells);
	}

	@Override
	public Stream<Integer> vertexStream() {
		return range(0, nCells).boxed();
	}

	@Override
	public int vertexCount() {
		return nCells;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NakedGrid<PassageWeight> makeFullGrid() {
		removeEdges();
		range(0, nCols).forEach(col -> {
			range(0, nRows).forEach(row -> {
				if (col + 1 < nCols) {
					connect(index(col, row), index(col + 1, row), E, true);
				}
				if (row + 1 < nRows) {
					connect(index(col, row), index(col, row + 1), S, true);
				}
			});
		});
		return this;
	}

	@Override
	public Stream<WeightedEdge<Integer, PassageWeight>> edgeStream() {
		Set<WeightedEdge<Integer, PassageWeight>> edgeSet = new HashSet<>();
		vertexStream().forEach(cell -> {
			Stream.of(Dir4.values()).forEach(dir -> {
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
	public Stream<WeightedEdge<Integer, PassageWeight>> fullGridEdgesPermuted() {
		List<WeightedEdge<Integer, PassageWeight>> edges = new ArrayList<>();
		range(0, nCols).forEach(col -> {
			range(0, nRows).forEach(row -> {
				if (col + 1 < nCols) {
					edges.add(new WeightedEdge<>(index(col, row), index(col + 1, row)));
				}
				if (row + 1 < nRows) {
					edges.add(new WeightedEdge<>(index(col, row), index(col, row + 1)));
				}
			});
		});
		shuffle(edges);
		return edges.stream();
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
	public Optional<WeightedEdge<Integer, PassageWeight>> edge(Integer p, Integer q) {
		checkCell(p);
		checkCell(q);
		return adjacent(p, q) ? Optional.of(new WeightedEdge<>(p, q)) : Optional.empty();
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		if (adjacent(p, q)) {
			throw new IllegalStateException("Cannot add edge twice");
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
		connections.clear();
	}

	@Override
	public Stream<Integer> adjVertices(Integer cell) {
		checkCell(cell);
		/*@formatter:off*/
		return Stream.of(Dir4.values())
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
		return nCols;
	}

	@Override
	public int numRows() {
		return nRows;
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
			return cell(nCols - 1, 0);
		case CENTER:
			return cell(nCols / 2, nRows / 2);
		case BOTTOM_LEFT:
			return cell(0, nRows - 1);
		case BOTTOM_RIGHT:
			return cell(nCols - 1, nRows - 1);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int col(Integer cell) {
		checkCell(cell);
		return cell % nCols;
	}

	@Override
	public int row(Integer cell) {
		checkCell(cell);
		return cell / nCols;
	}

	@Override
	public boolean isValidCol(int col) {
		return 0 <= col && col < nCols;
	}

	@Override
	public boolean isValidRow(int row) {
		return 0 <= row && row < nRows;
	}

	@Override
	public boolean areNeighbors(Integer either, Integer other) {
		return neighbors(either).anyMatch(neighbor -> neighbor.equals(other));
	}

	@Override
	public Stream<Integer> neighbors(Integer cell, Stream<Dir4> dirs) {
		return dirs.map(dir -> neighbor(cell, dir)).filter(Optional::isPresent).map(Optional::get);
	}

	@Override
	public Stream<Integer> neighbors(Integer cell) {
		return neighbors(cell, Arrays.stream(Dir4.values()));
	}

	@Override
	public Stream<Integer> neighborsPermuted(Integer cell) {
		return neighbors(cell, Arrays.stream(Dir4.valuesPermuted()));
	}

	@Override
	public Optional<Integer> neighbor(Integer cell, Dir4 dir) {
		checkCell(cell);
		int col = col(cell) + dir.dx();
		int row = row(cell) + dir.dy();
		if (isValidCol(col) && isValidRow(row)) {
			return Optional.of(index(col, row));
		}
		return Optional.empty();
	}

	@Override
	public boolean isConnected(Integer cell, Dir4 dir) {
		checkCell(cell);
		return connections.get(bit(cell, dir));
	}

	@Override
	public Optional<Dir4> direction(Integer source, Integer target) {
		checkCell(source);
		checkCell(target);
		/*@formatter:off*/
		return Stream.of(Dir4.values())
			.filter(dir -> neighbor(source, dir).isPresent())
			.filter(dir -> target.equals(neighbor(source, dir).get()))
			.findFirst();
		/*@formatter:on*/
	}
}