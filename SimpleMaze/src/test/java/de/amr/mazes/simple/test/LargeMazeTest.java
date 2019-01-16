package de.amr.mazes.simple.test;

import static de.amr.mazes.simple.MazeAlgorithms.createMazeByAldousBroder;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByBFS;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByBinaryTree;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByDFS;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByGrowingTree;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByKruskal;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByPrim;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByRecursiveDivision;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeBySidewinder;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

import de.amr.mazes.simple.graph.GraphFunctions;
import de.amr.mazes.simple.graph.GridGraph;
import de.amr.mazes.simple.graph.GridGraphImpl;

public class LargeMazeTest {

	static final int L = 1000;
	static final int M = 100;

	static GridGraph test(String name, Consumer<GridGraph> generator, int rows, int cols) {
		GridGraph grid = new GridGraphImpl(rows, cols);
		long start = System.nanoTime();
		generator.accept(grid);
		long time = System.nanoTime() - start;
		System.out
				.println(String.format("%20s: %,d vertices, (%d ms)", name, grid.numVertices(), time / 1_000_000));
		Assert.assertEquals("Wrong #edges", grid.numVertices() - 1, grid.numEdges());
		Assert.assertTrue(!GraphFunctions.containsCycle(grid));
		return grid;
	}

	@Test
	public void test_DFS() {
		test("DFS", grid -> createMazeByDFS(grid, 0), L, L);
	}

	@Test
	public void test_BFS() {
		test("BFS", grid -> createMazeByBFS(grid, 0), L, L);
	}

	@Test
	public void test_Kruskal() {
		test("Kruskal", grid -> createMazeByKruskal(grid), L, L);
	}

	@Test
	public void test_Prim() {
		test("Prim", grid -> createMazeByPrim(grid, 0), L, L);
	}

	@Test
	public void test_BinaryTree() {
		test("Binary Tree", grid -> createMazeByBinaryTree(grid), L, L);
	}

	@Test
	public void test_GrowingTree() {
		test("Growing Tree", grid -> createMazeByGrowingTree(grid, 0), L, L);
	}

	@Test
	public void test_Sidewinder() {
		test("Sidewinder", grid -> createMazeBySidewinder(grid), L, L);
	}

	@Test
	public void test_RecursiveDivision() {
		test("Recursive Division", grid -> createMazeByRecursiveDivision(grid), L, L);
	}

	@Test
	public void test_AldousBroder() {
		test("Aldous Broder", grid -> createMazeByAldousBroder(grid, 0), M, M);
	}

}