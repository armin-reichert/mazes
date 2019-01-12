package de.amr.mazes.simple.graph;

import java.util.BitSet;

/**
 * Stripped down grid graph implementation.
 * 
 * @author Armin Reichert
 */
public class GridGraph {

	public final int rows;
	public final int cols;
	private final BitSet edges;

	public GridGraph(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		edges = new BitSet(4 * rows * cols);
	}

	public int numVertices() {
		return rows * cols;
	}

	public int numEdges() {
		return edges.cardinality() / 2;
	}

	public int vertex(int row, int col) {
		return row * cols + col;
	}

	public int row(int vertex) {
		return vertex / cols;
	}

	public int col(int vertex) {
		return vertex % cols;
	}

	public int neighbor(int vertex, Dir dir) {
		int row = row(vertex), col = col(vertex);
		switch (dir) {
		case N:
			return row - 1 >= 0 ? vertex(row - 1, col) : -1;
		case E:
			return col + 1 < cols ? vertex(row, col + 1) : -1;
		case S:
			return row + 1 < rows ? vertex(row + 1, col) : -1;
		case W:
			return col - 1 >= 0 ? vertex(row, col - 1) : -1;
		default:
			throw new IllegalArgumentException();
		}
	}

	public boolean connected(int vertex, Dir dir) {
		return edges.get(4 * vertex + dir.ordinal());
	}

	public void connect(int vertex, Dir dir) {
		if (connected(vertex, dir)) {
			System.err.println(this);
			throw new IllegalStateException(String.format("Already connected: %s, %s", name(vertex), dir));
		}
		int neighbor = neighbor(vertex, dir);
		if (neighbor == -1) {
			throw new IllegalArgumentException(
					String.format("Cannot connect vertex %s towards %s", name(vertex), dir.name()));
		}
		edges.set(4 * vertex + dir.ordinal());
		edges.set(4 * neighbor + dir.opposite().ordinal());
	}

	public void connect(int vertex, int neighbor) {
		for (Dir dir : Dir.values()) {
			if (neighbor == neighbor(vertex, dir)) {
				connect(vertex, dir);
				return;
			}
		}
		throw new IllegalStateException();
	}

	public String name(int vertex) {
		return String.format("(%d,%d)", row(vertex), col(vertex));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int vertex = 0; vertex < numVertices(); ++vertex) {
			for (Dir dir : Dir.values()) {
				if (connected(vertex, dir)) {
					sb.append(name(vertex)).append("->").append(name(neighbor(vertex, dir))).append("\n");
				}
			}
		}
		return sb.toString();
	}
}