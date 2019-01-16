package de.amr.mazes.simple.graph;

import java.util.BitSet;
import java.util.function.IntConsumer;

import de.amr.easy.data.Partition;

public class GraphFunctions {

	public static void dfs(GridGraph grid, int vertex, BitSet visited, IntConsumer fnAction) {
		visited.set(vertex);
		fnAction.accept(vertex);
		System.out.println("Visit " + grid.name(vertex));
		for (Dir dir : Dir.values()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && grid.connected(vertex, dir) && !visited.get(neighbor)) {
				dfs(grid, neighbor, visited, fnAction);
			}
		}
	}

	public static boolean containsCycle(GridGraph grid) {
		Partition<Integer> p = new Partition<>();
		for (Edge edge : grid.edges()) {
			int u = edge.either, v = edge.other;
			if (p.find(u) == p.find(v)) {
				return true;
			}
			p.union(u, v);
		}
		return false;
	}

	public static void prettyPrint(GridGraph grid) {
		for (int row = 0; row < grid.rows; ++row) {
			for (int col = 0; col < grid.cols; ++col) {
				int vertex = grid.vertex(row, col);
				System.out.print(grid.name(vertex));
				System.out.print(grid.connected(vertex, Dir.E) ? "\u2014" : " ");
			}
			if (row < grid.rows - 1) {
				System.out.println();
				for (int col = 0; col < grid.cols; ++col) {
					int below = grid.vertex(row + 1, col);
					System.out.print(grid.connected(below, Dir.N) ? "  |   " : "      ");
				}
			}
			System.out.println();
		}
		System.out.println(
				String.format("Num vertices: %d (%d rows, %d cols)", grid.numVertices(), grid.rows, grid.cols));
		System.out.println("Num edges: " + grid.numEdges());
	}
}
