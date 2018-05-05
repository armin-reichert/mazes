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

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.impl.BareGrid;

public class GridTraversalTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private BareGrid2D<Integer> grid;

	@Before
	public void setUp() {
		grid = new BareGrid<>(WIDTH, HEIGHT);
		grid.fill();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testBFS() {
		BreadthFirstTraversal<WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(grid, grid.cell(CENTER));
		grid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == UNVISITED);
		});
		bfs.traverseGraph();
		grid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == COMPLETED);
		});
	}

	@Test
	public void testDFS() {
		Integer source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal<WeightedEdge<Integer>> dfs = new DepthFirstTraversal<>(grid, source, target);
		grid.vertexStream().forEach(cell -> {
			assertTrue(dfs.getState(cell) == UNVISITED);
		});
		dfs.traverseGraph();
		assertTrue(dfs.getState(source) == COMPLETED || dfs.getState(target) == COMPLETED);
		for (Integer cell : dfs.findPath(target)) {
			assertTrue(dfs.getState(cell) == COMPLETED);
		}
	}
}
