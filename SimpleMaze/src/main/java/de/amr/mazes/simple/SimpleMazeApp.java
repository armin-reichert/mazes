package de.amr.mazes.simple;

import static de.amr.mazes.simple.MazeAlgorithms.*;
import static de.amr.mazes.simple.graph.GraphFunctions.printGrid;

import java.util.BitSet;
import java.util.function.Consumer;

import de.amr.mazes.simple.graph.GridGraph;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 *
 */
public class SimpleMazeApp {

	public static void main(String[] args) {
		GridGraph maze;
		maze = test("Recursive DFS", grid -> createMazeByDFSRecursive(grid, 0, new BitSet()), 8, 8);
		printGrid(maze);
		maze = test("Binary Tree", grid -> createMazeByBinaryTree(grid), 8, 8);
		printGrid(maze);
	}

	static GridGraph test(String name, Consumer<GridGraph> generator, int rows, int cols) {
		GridGraph grid = new GridGraph(rows, cols);
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