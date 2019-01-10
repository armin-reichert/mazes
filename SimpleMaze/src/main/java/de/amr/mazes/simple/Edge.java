package de.amr.mazes.simple;

public class Edge implements Comparable<Edge> {

	public final int either;
	public final int other;
	public final int weight;

	public Edge(int either, int other) {
		this(either, other, 0);
	}

	public Edge(int either, int other, int weight) {
		this.either = either;
		this.other = other;
		this.weight = weight;
	}

	@Override
	public int compareTo(Edge other) {
		return Integer.compare(weight, other.weight);
	}
}