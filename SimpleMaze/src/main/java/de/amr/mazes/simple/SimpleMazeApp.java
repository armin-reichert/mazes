package de.amr.mazes.simple;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import de.amr.mazes.simple.graph.GraphFunctions;
import de.amr.mazes.simple.graph.GridGraph;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 */
public class SimpleMazeApp {

	public static void main(String[] args) {
		IntStream.range(0, 10).forEach(i -> {
			createMaze("AldousBroder", grid -> MazeAlgorithms.createMazeByAldousBroder(grid, 0), 100, 100);
		});
		GraphFunctions.prettyPrint(
				createMaze("AldousBroder", grid -> MazeAlgorithms.createMazeByAldousBroder(grid, 0), 10, 10));
		GraphFunctions.prettyPrint(
				createMaze("Growing Tree", grid -> MazeAlgorithms.createMazeByGrowingTree(grid, 0), 10, 10));
		GraphFunctions.prettyPrint(
				createMaze("Recursive Division", grid -> MazeAlgorithms.createMazeByRecursiveDivision(grid), 8, 8));
	}

	static GridGraph createMaze(String name, Consumer<GridGraph> generator, int rows, int cols) {
		GridGraph grid = new GridGraph(rows, cols);
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
		return grid;
	}
}