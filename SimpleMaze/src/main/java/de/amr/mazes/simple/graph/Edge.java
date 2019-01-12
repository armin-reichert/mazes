package de.amr.mazes.simple.graph;

public class Edge implements Comparable<Edge> {

	private final GridGraph grid;
	public final int either;
	public final int other;
	public final int weight;

	public Edge(GridGraph grid, int either, int other) {
		this(grid, either, other, 0);
	}

	public Edge(GridGraph grid, int either, int other, int weight) {
		this.grid = grid;
		this.either = either;
		this.other = other;
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge other) {
		return Integer.compare(weight, other.weight);
	}

	@Override
	public String toString() {
		return String.format("%s->%s(%d)", grid.name(either), grid.name(other), weight);
	}
}