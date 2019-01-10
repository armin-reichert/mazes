package de.amr.mazes.simple;

import java.util.BitSet;
import java.util.function.IntConsumer;

public class Goodies {

	public static void dfs(Grid grid, int vertex, BitSet visited, IntConsumer fnAction) {
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

	public static void printGrid(Grid grid) {
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
		System.out.println("Num edges " + grid.numEdges());
	}

}
