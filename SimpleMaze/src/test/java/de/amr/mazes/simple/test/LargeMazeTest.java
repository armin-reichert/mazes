package de.amr.mazes.simple.test;

import static de.amr.mazes.simple.MazeAlgorithms.*;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByBinaryTree;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByDFS;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByKruskal;
import static de.amr.mazes.simple.MazeAlgorithms.createMazeByPrim;

import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Test;

import de.amr.mazes.simple.graph.GridGraph;

public class LargeMazeTest {

	static GridGraph test(String name, Consumer<GridGraph> generator, int rows, int cols) {
		GridGraph grid = new GridGraph(rows, cols);
		long start = System.currentTimeMillis();
		generator.accept(grid);
		long time = System.currentTimeMillis() - start;
		System.out.println(String.format("%15s: %,d vertices, (%d ms)", name, grid.numVertices(), time));
		Assert.assertEquals("Wrong #edges", grid.numVertices() - 1, grid.numEdges());
		return grid;
	}

	@Test
	public void test_DFS() {
		test("DFS", grid -> createMazeByDFS(grid, 0), 1000, 1000);
	}

	@Test
	public void test_BFS() {
		test("BFS", grid -> createMazeByBFS(grid, 0), 1000, 1000);
	}

	@Test
	public void test_Kruskal() {
		test("Kruskal", grid -> createMazeByKruskal(grid), 1000, 1000);
	}

	@Test
	public void test_Prim() {
		test("Prim", grid -> createMazeByPrim(grid, 0), 1000, 1000);
	}

	@Test
	public void test_BinaryTree() {
		test("Binary Tree", grid -> createMazeByBinaryTree(grid), 1_000, 1_000);
	}
	
	@Test
	public void test_AldousBroder() {
		test("Aldous Broder", grid -> createMazeByAldousBroder(grid, 0), 1000, 1000);
	}

}