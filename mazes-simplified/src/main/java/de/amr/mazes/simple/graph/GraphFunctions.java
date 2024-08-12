package de.amr.mazes.simple.graph;

import java.io.PrintStream;
import java.util.BitSet;
import java.util.function.IntConsumer;


/**
 * @author Armin Reichert
 */
public interface GraphFunctions {

	public static void dfs(GridGraph grid, int vertex, BitSet visited, IntConsumer fnAction) {
		visited.set(vertex);
		fnAction.accept(vertex);
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
			int u = edge.either();
			int v = edge.other();
			if (p.find(u) == p.find(v)) {
				return true;
			}
			p.union(u, v);
		}
		return false;
	}

	public static void prettyPrint(GridGraph grid, PrintStream p) {
		for (int row = 0; row < grid.numRows(); ++row) {
			for (int col = 0; col < grid.numCols(); ++col) {
				int vertex = grid.vertex(row, col);
				p.print(grid.name(vertex));
				p.print(grid.connected(vertex, Dir.E) ? "-" : " ");
			}
			if (row < grid.numRows() - 1) {
				p.println();
				for (int col = 0; col < grid.numCols(); ++col) {
					int below = grid.vertex(row + 1, col);
					p.print(grid.connected(below, Dir.N) ? "  |   " : "      ");
				}
			}
			p.println();
		}
	}
}
