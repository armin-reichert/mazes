package de.amr.mazes.simple;

import static de.amr.mazes.simple.MazeAlgorithms.createMazeByDFSRecursive;

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
		Grid maze = test("Recursive DFS", grid -> createMazeByDFSRecursive(grid, 0, new BitSet()), 8, 8);
		Goodies.printGrid(maze);
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