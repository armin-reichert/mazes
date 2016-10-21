package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal;
import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.impl.NakedGrid;

public class GridTraversalTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private NakedGrid2D grid;

	@Before
	public void setUp() {
		grid = new NakedGrid(WIDTH, HEIGHT);
		grid.makeFullGrid();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testBFS() {
		BreadthFirstTraversal<Integer, WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(grid, grid.cell(CENTER));
		grid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == UNVISITED);
		});
		bfs.run();
		grid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == COMPLETED);
		});
	}

	@Test
	public void testDFS() {
		Integer source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal<Integer, WeightedEdge<Integer>> dfs = new DepthFirstTraversal<>(grid, source, target);
		grid.vertexStream().forEach(cell -> {
			assertTrue(dfs.getState(cell) == UNVISITED);
		});
		dfs.run();
		assertTrue(dfs.getState(source) == COMPLETED || dfs.getState(target) == COMPLETED);
		for (Integer cell : dfs.findPath(target)) {
			assertTrue(dfs.getState(cell) == COMPLETED);
		}
	}
}
