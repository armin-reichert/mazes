package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import org.junit.Test;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.mst.KruskalMST;

public class LargeMazesTests {

	private void test_Kruskal(int cols, int rows) {
		Grid2D<TraversalState, Integer> grid = new Grid<>(cols, rows, Top4.get(), UNVISITED);
		new KruskalMST(grid).run(0);
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
	public void test_Kruskal_400_000() {
		test_Kruskal(400, 1000);
	}

	@Test
	public void test_Kruskal_800_000() {
		test_Kruskal(800, 1000);
	}
}
