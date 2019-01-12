package de.amr.mazes.simple;

import static de.amr.mazes.simple.MazeAlgorithms.createMazeByPrim;

import java.util.function.Consumer;

import de.amr.mazes.simple.graph.GraphFunctions;
import de.amr.mazes.simple.graph.GridGraph;

/**
 * Sample app for stripped down maze generation implementation.
 * 
 * @author Armin Reichert
 *
 */
public class SimpleMazeApp {

	public static void main(String[] args) {
		GridGraph maze = createMaze("Prim", grid -> createMazeByPrim(grid, 0), 100, 200);
		GraphFunctions.printGrid(maze);
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
		return grid;
	}
}