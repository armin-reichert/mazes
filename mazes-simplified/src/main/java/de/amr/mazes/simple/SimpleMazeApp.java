package de.amr.mazes.simple;

import static de.amr.mazes.simple.graph.GraphFunctions.prettyPrint;

import java.io.PrintStream;
import java.util.function.Consumer;

import de.amr.mazes.simple.graph.GraphFunctions;
import de.amr.mazes.simple.graph.GridGraphImpl;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 */
public class SimpleMazeApp {

	static int numRows = 10;
	static int numCols = 20;
	static PrintStream p = System.out;

	public static void main(String[] args) {
		maze(p, "AldousBroder", grid -> MazeAlgorithms.createMazeByAldousBroder(grid, 0), numRows, numCols);
		maze(p, "Growing Tree", grid -> MazeAlgorithms.createMazeByGrowingTree(grid, 0), numRows, numCols);
		maze(p, "Sidewinder", grid -> MazeAlgorithms.createMazeBySidewinder(grid), numRows, numCols);
		maze(p, "Recursive Division", grid -> MazeAlgorithms.createMazeByRecursiveDivision(grid), numRows, numCols);
		maze(p, "Wilson", grid -> MazeAlgorithms.createMazeByWilson(grid), numRows, numCols);
	}

	static void maze(PrintStream p, String generatorName, Consumer<GridGraphImpl> generator, int numRows, int numCols) {

		GridGraphImpl grid = new GridGraphImpl(numRows, numCols);

		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;

		p.println();
		p.println("Graph:     %d vertices, %d edges".formatted(grid.numVertices(), grid.numEdges()));
		p.println("Algorithm: %s".formatted(generatorName));
		p.println("Time:      %d milliseconds".formatted(time));
		prettyPrint(grid, p);

		if (grid.numEdges() != grid.numVertices() - 1) {
			p.println("Wrong #edges: %d (expected %d)".formatted(grid.numEdges(), grid.numVertices() - 1));
		}

		if (GraphFunctions.containsCycle(grid)) {
			p.println("No maze. Graph contains cycle");
		}
	}
}