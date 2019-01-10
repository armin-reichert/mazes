package de.amr.mazes.simple;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;

/**
 * Maze generation functions.
 * 
 * @author Armin Reichert
 */
public class MazeGeneration {

	public static void createMazeByRecursiveDFS(Grid grid, int vertex, BitSet visited) {
		visited.set(vertex);
		for (Dir dir : Dir.shuffled()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				grid.connect(vertex, dir);
				createMazeByRecursiveDFS(grid, neighbor, visited);
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
}