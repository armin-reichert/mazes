package de.amr.mazes.simple;

import java.util.BitSet;

/**
 * Stripped down grid graph implementation.
 * 
 * @author Armin Reichert
 */
public class Grid {

	public final int rows;
	public final int cols;
	private final BitSet edges;

	public Grid(int rows, int cols) {
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
		return row * rows + col;
	}

	public int row(int vertex) {
		return vertex / rows;
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

	public void connect(int vertex, Dir dir) {
		int neighbor = neighbor(vertex, dir);
		if (neighbor != -1) {
			edges.set(4 * vertex + dir.ordinal());
			edges.set(4 * neighbor + dir.opposite().ordinal());
		} else {
			throw new IllegalArgumentException(
					String.format("Cannot connect vertex %s towards %s", name(vertex), dir.name()));
		}
	}

	public boolean connected(int vertex, Dir dir) {
		int neighbor = neighbor(vertex, dir);
		return neighbor != -1 && edges.get(4 * vertex + dir.ordinal());
	}

	public String name(int vertex) {
		return String.format("(%d,%d)", row(vertex), col(vertex));
	}
}