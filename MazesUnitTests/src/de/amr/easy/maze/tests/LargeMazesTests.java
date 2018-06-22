package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import org.junit.Test;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.mst.KruskalMST;

public class LargeMazesTests {

	private void test_Kruskal(int cols, int rows) {
		GridGraph2D<TraversalState, Void> grid = new GridGraph<>(cols, rows, Top4.get(), SimpleEdge::new);
		grid.setDefaultVertex(UNVISITED);
		new KruskalMST(grid).run(-1);
	}

	private void test_RecursiveDivision(int cols, int rows) {
		GridGraph2D<TraversalState, Void> grid = new GridGraph<>(cols, rows, Top4.get(), SimpleEdge::new);
		grid.setDefaultVertex(COMPLETED);
		new RecursiveDivision(grid).run(-1);
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