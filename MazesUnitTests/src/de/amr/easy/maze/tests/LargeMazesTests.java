package de.amr.easy.maze.tests;

import org.junit.Test;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.mst.KruskalMST;

public class LargeMazesTests {

	private void test_Kruskal(int cols, int rows) {
		Grid2D<TraversalState, Integer> grid = new Grid<>(cols, rows, Top4.get(), TraversalState.UNVISITED);
		new KruskalMST(grid).run(grid.cell(0, 0));
	}

	@Test
	public void test_Kruskal_100_000() {
		test_Kruskal(100, 1000);
	}

	@Test
	public void test_Kruskal_200_000() {
		test_Kruskal(200, 1000);
	}

	@Test
	public void test_Kruskal_300_000() {
		test_Kruskal(300, 1000);
	}

	@Test
	public void test_Kruskal_400_000() {
		test_Kruskal(400, 1000);
	}

	@Test
	public void test_Kruskal_500_000() {
		test_Kruskal(500, 1000);
	}

	@Test
	public void test_Kruskal_600_000() {
		test_Kruskal(600, 1000);
	}

	@Test
	public void test_Kruskal_700_000() {
		test_Kruskal(700, 1000);
	}

	@Test
	public void test_Kruskal_800_000() {
		test_Kruskal(800, 1000);
	}

	@Test
	public void test_Kruskal_900_000() {
		test_Kruskal(900, 1000);
	}

	@Test
	public void test_Kruskal_1_000_000() {
		test_Kruskal(1000, 1000);
	}
}
