package de.amr.mazes.simple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

import de.amr.easy.data.Partition;

/**
 * Maze generation functions.
 * 
 * @author Armin Reichert
 */
public class MazeGeneration {

	private static class Edge {

		int either, other;

		public Edge(int either, int other) {
			this.either = either;
			this.other = other;
		}
	}

	public static void createMazeByDFSRecursive(Grid grid, int vertex, BitSet visited) {
		visited.set(vertex);
		for (Dir dir : Dir.shuffled()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				grid.connect(vertex, dir);
				createMazeByDFSRecursive(grid, neighbor, visited);
			}
		}
	}

	public static void createMazeByDFS(Grid grid, int startVertex) {
		BitSet visited = new BitSet();
		Deque<Integer> stack = new ArrayDeque<>();
		visited.set(startVertex);
		stack.push(startVertex);
		while (!stack.isEmpty()) {
			int vertex = stack.pop();
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					stack.push(neighbor);
				}
			}
		}
	}

	public static void createMazeByBFS(Grid grid, int startVertex) {
		BitSet visited = new BitSet();
		List<Integer> frontier = new ArrayList<>();
		Random rnd = new Random();
		visited.set(startVertex);
		frontier.add(startVertex);
		while (!frontier.isEmpty()) {
			int vertex = frontier.remove(rnd.nextInt(frontier.size()));
			for (Dir dir : Dir.values()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					frontier.add(neighbor);
				}
			}
		}
	}

	public static void createMazeByKruskal(Grid grid) {
		List<Edge> edges = new ArrayList<>();
		for (int row = 0; row < grid.rows; ++row) {
			for (int col = 0; col < grid.cols; ++col) {
				int vertex = grid.vertex(row, col);
				if (row > 0) {
					edges.add(new Edge(vertex, grid.neighbor(vertex, Dir.N)));
				}
				if (col > 0) {
					edges.add(new Edge(vertex, grid.neighbor(vertex, Dir.W)));
				}
			}
		}
		Collections.shuffle(edges);
		Partition<Integer> forest = new Partition<>();
		for (Edge edge : edges) {
			int u = edge.either, v = edge.other;
			if (forest.find(u) != forest.find(v)) {
				grid.connect(u, v);
				forest.union(u, v);
			}
		}
	}
}