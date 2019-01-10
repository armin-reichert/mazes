package de.amr.mazes.simple;

import static de.amr.mazes.simple.MazeGeneration.createMazeByDFS;
import static de.amr.mazes.simple.MazeGeneration.createMazeByRecursiveDFS;

import java.util.BitSet;
import java.util.function.Consumer;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 *
 */
public class SimpleMazeApp {

	public static void main(String[] args) {
		test("Recursive DFS", grid -> createMazeByRecursiveDFS(grid, 0, new BitSet()), 90, 90);
		test("Non-Recursive DFS", grid -> createMazeByDFS(grid, 0), 10_000, 1000);
	}

	static Grid test(String name, Consumer<Grid> generator, int rows, int cols) {
		Grid grid = new Grid(rows, cols);
		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;
		System.out.println(String.format(name + ": %,d vertices, time %d ms", grid.numVertices(), time));
		if (grid.numEdges() != grid.numVertices() - 1) {
			throw new IllegalStateException(
					String.format("Wrong #edges: %d (expected %d)", grid.numEdges(), grid.numVertices() - 1));
		}
		return grid;
	}
}