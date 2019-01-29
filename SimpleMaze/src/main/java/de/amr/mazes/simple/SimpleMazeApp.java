package de.amr.mazes.simple;

import static de.amr.mazes.simple.MazeAlgorithms.createMazeByAldousBroder;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByGrowingTree;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByRecursiveDivision;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeBySidewinder;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByWilson;
import static de.amr.mazes.simple.graph.GraphFunctions.prettyPrint;

import java.util.function.Consumer;

import de.amr.mazes.simple.graph.GraphFunctions;
import de.amr.mazes.simple.graph.GridGraphImpl;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 */
public class SimpleMazeApp {

	public static void main(String[] args) {
		printMaze("AldousBroder", grid -> createMazeByAldousBroder(grid, 0), 10, 10);
		printMaze("Growing Tree", grid -> createMazeByGrowingTree(grid, 0), 10, 10);
		printMaze("Sidewinder", grid -> createMazeBySidewinder(grid), 10, 10);
		printMaze("Recursive Division", grid -> createMazeByRecursiveDivision(grid), 10, 10);
		printMaze("Wilson", grid -> createMazeByWilson(grid), 10, 10);
	}

	static void printMaze(String name, Consumer<GridGraphImpl> generator, int rows, int cols) {
		GridGraphImpl grid = new GridGraphImpl(rows, cols);
		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;
		System.out.println(String.format(name + ": %,d vertices, %d ms", grid.numVertices(), time));
		if (grid.numEdges() != grid.numVertices() - 1) {
			throw new IllegalStateException(
					String.format("Wrong #edges: %d (expected %d)", grid.numEdges(), grid.numVertices() - 1));
		}
		if (GraphFunctions.containsCycle(grid)) {
			throw new IllegalStateException("Graph contains cycle");
		}
		prettyPrint(grid);
	}
}