package de.amr.easy.maze.tests;

import static de.amr.graph.core.api.TraversalState.UNVISITED;

import org.junit.Test;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.impl.GridFactory;
import de.amr.graph.grid.impl.Top4;
import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.mst.KruskalMST;
import de.amr.util.StopWatch;

public class LargeMazesTest {

	private void test_Kruskal(int numCols, int numRows) {
		GridGraph2D<TraversalState, Integer> grid = GridFactory.emptyGrid(numCols, numRows, Top4.get(), UNVISITED,
				0);
		StopWatch watch = new StopWatch();
		watch.measure(() -> new KruskalMST(grid).createMaze(0, 0));
		System.out.println(String.format("Kruskal: %d vertices (%.0f ms)", numCols * numRows, watch.getMillis()));
	}

	private void test_RecursiveDivision(int numCols, int numRows) {
		GridGraph2D<TraversalState, Integer> grid = GridFactory.emptyGrid(numCols, numRows, Top4.get(), UNVISITED,
				0);
		StopWatch watch = new StopWatch();
		watch.measure(() -> new RecursiveDivision(grid).createMaze(0, 0));
		System.out.println(
				String.format("RecursiveDivision: %d vertices (%.0f ms)", numCols * numRows, watch.getMillis()));
	}

	@Test
	public void test_Kruskal_100_000() {
		test_Kruskal(100, 1000);
	}

	@Test
	public void test_Kruskal_500_000() {
		test_Kruskal(500, 1000);
	}

	@Test
	public void test_Kruskal_1_000_000() {
		test_Kruskal(1000, 1000);
	}

	@Test
	public void test_RecursiveDivision_100_000() {
		test_RecursiveDivision(100, 1000);
	}

	@Test
	public void test_RecursiveDivision_500_000() {
		test_RecursiveDivision(500, 1000);
	}

	@Test
	public void test_RecursiveDivision_1_000_000() {
		test_RecursiveDivision(1000, 1000);
	}
}