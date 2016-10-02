package de.amr.easy.grid.impl;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;

/**
 * A {@link Grid2D} implementation where the grid cells are represented by their coordinates (not
 * explicitly stored) and the edges are stored in a single bit-set.
 * 
 * @author Armin Reichert
 */
public class RawGrid implements Grid2D<Integer, DefaultEdge<Integer>> {

	private final int numCols;
	private final int numRows;
	private final BitSet edges;

	private void checkCell(Integer cell) {
		if (cell == null || cell < 0 || cell >= numCells()) {
			throw new IllegalArgumentException("Invalid cell: " + cell);
		}
	}

	private void checkEdge(Edge<Integer> edge) {
		if (edge == null) {
			throw new IllegalArgumentException("Invalid edge: null");
		}
		Integer either = edge.either(), other = edge.other(either);
		checkCell(either);
		checkCell(other);
	}

	private int cellIndex(int col, int row) {
		return col + row * numCols;
	}

	private int bit(int cell, Direction dir) {
		return cell * 4 + dir.ordinal();
	}

	private void setConnected(int p, int q, Direction dir, boolean connected) {
		edges.set(bit(p, dir), connected);
		edges.set(bit(q, dir.inverse()), connected);
	}

	public RawGrid(int numCols, int numRows) {
		if (numCols < 0) {
			throw new IllegalArgumentException("Invalid number of columns: " + numCols);
		}
		if (numRows < 0) {
			throw new IllegalArgumentException("Invalid number of rows: " + numRows);
		}
		this.numCols = numCols;
		this.numRows = numRows;
		edges = new BitSet(4 * numCols * numRows);
	}

	@Override
	public Stream<Integer> vertexStream() {
		return IntStream.range(0, numCells()).boxed();
	}

	@Override
	public int vertexCount() {
		return numCells();
	}

	private Set<DefaultEdge<Integer>> createEdgeSet() {
		Set<DefaultEdge<Integer>> edgeSet = new HashSet<>();
		vertexStream().forEach(cell -> {
			Stream.of(Direction.values()).forEach(dir -> {
				if (isCellConnected(cell, dir)) {
					Integer neighbor = neighbor(cell, dir);
					if (neighbor != null && cell < neighbor) {
						edgeSet.add(new DefaultEdge<>(cell, neighbor));
					}
				}
			});
		});
		return edgeSet;
	}

	@Override
	public Stream<DefaultEdge<Integer>> edgeStream() {
		return createEdgeSet().stream(); // TODO is there a more efficient way?
	}

	@Override
	public int edgeCount() {
		return edges.cardinality() / 2; // two bits are set for each undirected edge
	}

	@Override
	public void addVertex(Integer p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<DefaultEdge<Integer>> edge(Integer p, Integer q) {
		checkCell(p);
		checkCell(q);
		return adjacent(p, q) ? Optional.of(new DefaultEdge<>(p, q)) : Optional.empty();
	}

	@Override
	public void addEdge(DefaultEdge<Integer> edge) {
		checkEdge(edge);
		Integer p = edge.either(), q = edge.other(p);
		if (adjacent(p, q)) {
			throw new IllegalStateException("Duplicate edge: " + edge);
		}
		setConnected(p, q, direction(p, q), true);
	}

	@Override
	public void removeEdge(DefaultEdge<Integer> edge) {
		checkEdge(edge);
		Integer p = edge.either(), q = edge.other(p);
		if (!adjacent(p, q)) {
			throw new IllegalStateException("Unknown edge: " + edge);
		}
		setConnected(p, q, direction(p, q), false);
	}

	@Override
	public void removeEdges() {
		edges.clear();
	}

	@Override
	public Stream<Integer> adjVertices(Integer cell) {
		checkCell(cell);
		/*@formatter:off*/
		return Stream.of(Direction.values())
			.filter(dir -> isCellConnected(cell, dir))
			.map(dir -> neighbor(cell, dir));
		/*@formatter:on*/
	}

	@Override
	public boolean adjacent(Integer p, Integer q) {
		checkCell(p);
		checkCell(q);
		/*@formatter:off*/
		return Stream.of(Direction.values())
			.filter(dir -> isCellConnected(p, dir))
			.map(dir -> neighbor(p, dir))
			.anyMatch(neighbor -> neighbor.equals(q));
		/*@formatter:on*/
	}

	@Override
	public int degree(Integer cell) {
		checkCell(cell);
		/*@formatter:off*/
		return (int) Stream.of(Direction.values())
			.filter(dir -> isCellConnected(cell, dir))
			.count();
		/*@formatter:on*/
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
		return cellIndex(col, row);
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
	public Integer neighbor(Integer cell, Direction dir) {
		checkCell(cell);
		int col = col(cell) + dir.dx;
		int row = row(cell) + dir.dy;
		if (isValidCol(col) && isValidRow(row)) {
			return cellIndex(col, row);
		}
		return null;
	}

	@Override
	public boolean isCellConnected(Integer cell, Direction dir) {
		checkCell(cell);
		return edges.get(bit(cell, dir));
	}

	@Override
	public Direction direction(Integer source, Integer target) {
		checkCell(source);
		checkCell(target);
		/*@formatter:off*/
		return Stream.of(Direction.values())
			.filter(dir -> target.equals(neighbor(source, dir)))
			.findFirst()
			.orElse(null);
		/*@formatter:on*/
	}

	@Override
	public void fillAllEdges() {
		removeEdges();
		for (int col = 0; col < numCols; ++col) {
			for (int row = 0; row < numRows; ++row) {
				if (col + 1 < numCols) {
					setConnected(cellIndex(col, row), cellIndex(col + 1, row), Direction.E, true);
				}
				if (row + 1 < numRows) {
					setConnected(cellIndex(col, row), cellIndex(col, row + 1), Direction.S, true);
				}
			}
		}
	}
}