package de.amr.mazes.simple.test;

import static de.amr.mazes.simple.MazeGeneration.createMazeByDFS;
import static de.amr.mazes.simple.MazeGeneration.createMazeByKruskal;

import java.util.function.Consumer;

import org.junit.Test;

import de.amr.mazes.simple.Grid;

public class LargeMazeTest {

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

	// @Test
	public void test_DFS_100_000() {
		test("Non-Recursive DFS", grid -> createMazeByDFS(grid, 0), 100, 1000);
	}

	@Test
	public void test_DFS_1_000_000() {
		test("Non-Recursive DFS", grid -> createMazeByDFS(grid, 0), 1000, 1000);
	}

	@Test
	public void test_DFS_10_000_000() {
		test("Non-Recursive DFS", grid -> createMazeByDFS(grid, 0), 10_000, 1000);
	}

	@Test
	public void test_Kruskal_1_000_000() {
		test("Kruskal", grid -> createMazeByKruskal(grid), 1000, 1000);
	}
}
