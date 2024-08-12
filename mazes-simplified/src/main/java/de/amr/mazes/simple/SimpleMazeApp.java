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
		maze(p, "Sidewinder", MazeAlgorithms::createMazeBySidewinder, numRows, numCols);
		maze(p, "Recursive Division", MazeAlgorithms::createMazeByRecursiveDivision, numRows, numCols);
		maze(p, "Wilson", MazeAlgorithms::createMazeByWilson, numRows, numCols);
	}

	static void maze(PrintStream p, String generatorName, Consumer<GridGraphImpl> generator, int numRows, int numCols) {

		GridGraphImpl grid = new GridGraphImpl(numRows, numCols);

		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;

		p.println();
		p.printf("Graph:     %d vertices, %d edges%n", grid.numVertices(), grid.numEdges());
		p.printf("Algorithm: %s%n", generatorName);
		p.printf("Time:      %d milliseconds%n", time);
		prettyPrint(grid, p);

		if (grid.numEdges() != grid.numVertices() - 1) {
			p.printf("Wrong #edges: %d (expected %d)%n", grid.numEdges(), grid.numVertices() - 1);
		}

		if (GraphFunctions.containsCycle(grid)) {
			p.println("No maze. Graph contains cycle");
		}
	}
}