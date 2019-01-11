package de.amr.mazes.simple.test;

import static de.amr.mazes.simple.MazeAlgorithms.createMazeByBFS;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByBinaryTree;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByDFS;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByKruskal;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByPrim;

import java.util.function.Consumer;

import org.junit.Test;

import de.amr.mazes.simple.Grid;
import de.amr.mazes.simple.MazeAlgorithms;

public class LargeMazeTest {

	static Grid test(String name, Consumer<Grid> generator, int rows, int cols) {
		Grid grid = new Grid(rows, cols);
		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;
		System.out.println(String.format("%10s: %,d vertices, time %d ms", name, grid.numVertices(), time));
		if (grid.numEdges() != grid.numVertices() - 1) {
			throw new IllegalStateException(
					String.format("Wrong #edges: %d (expected %d)", grid.numEdges(), grid.numVertices() - 1));
		}
		return grid;
	}

	@Test
	public void test_DFS_1_000_000() {
		test("DFS", grid -> createMazeByDFS(grid, 0), 1000, 1000);
	}

	@Test
	public void test_DFS_10_000_000() {
		test("DFS", grid -> createMazeByDFS(grid, 0), 10_000, 1000);
	}

	@Test
	public void test_BFS_1_000_000() {
		test("BFS", grid -> createMazeByBFS(grid, 0), 1000, 1000);
	}

	@Test
	public void test_Kruskal_1_000_000() {
		test("Kruskal", grid -> createMazeByKruskal(grid), 1000, 1000);
	}

	@Test
	public void test_Prim_10_000() {
		test("Prim", grid -> createMazeByPrim(grid, 0), 100, 100);
	}

	@Test
	public void test_BinaryTree_1_000_000() {
		test("Binary Tree", grid -> createMazeByBinaryTree(grid), 1000, 1000);
	}
}