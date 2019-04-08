package de.amr.easy.maze.tests;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.core.api.TraversalState.VISITED;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.impl.GridGraph;
import de.amr.graph.pathfinder.impl.AStarSearch;
import de.amr.graph.pathfinder.impl.BestFirstSearch;
import de.amr.graph.util.GraphUtils;
import de.amr.maze.alg.core.UnobservableGridFactory;
import de.amr.maze.alg.traversal.IterativeDFS;
import de.amr.maze.alg.traversal.RandomBFS;

/**
 * Test case for {@link GridGraph}
 * 
 * @author Armin Reichert
 */
public class MazeTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;
	private static final int K = 8;
	private static final int N = 1 << K; // N = 2^K

	private static void assertState(IntStream cells, Function<Integer, TraversalState> fnSupplyState,
			TraversalState... expected) {
		cells.forEach(cell -> assertTrue(Arrays.stream(expected).anyMatch(s -> s == fnSupplyState.apply(cell))));
	}

	private GridGraph2D<TraversalState, Integer> grid;

	@Before
	public void setUp() {
		grid = UnobservableGridFactory.get().emptyGrid(WIDTH, HEIGHT, UNVISITED);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCycleChecker() {
		// create a spanning tree
		new RandomBFS(grid).createMaze(0, 0);
		assertFalse(GraphUtils.containsCycle(grid));

		// Find vertex with non-adjacent neighbor. Adding an edge to this neighbor produces a cycle.
		/*@formatter:off*/
		grid.vertices()
			.filter(cell -> grid.neighbors(cell).anyMatch(neighbor -> !grid.adjacent(cell, neighbor)))
			.findAny()
			.ifPresent(cell -> 	
				grid.neighbors(cell)
					.filter(neighbor -> !grid.adjacent(cell, neighbor))
					.findAny()
					.ifPresent(neighbor -> grid.addEdge(cell, neighbor)));
		/*@formatter:on*/

		// now there must be a cycle
		assertTrue(GraphUtils.containsCycle(grid));
	}

	@Test
	public void testBestFS() {
		grid = UnobservableGridFactory.get().emptyGrid(N, N, UNVISITED);
		new IterativeDFS(grid).createMaze(0, 0);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		BestFirstSearch best = new BestFirstSearch(grid, x -> grid.manhattan(x, target));
		assertState(grid.vertices(), best::getState, UNVISITED);
		best.exploreGraph(source);
		assertState(grid.vertices(), best::getState, VISITED, COMPLETED);
		best.findPath(source, target);
	}

	@Test
	public void testAStar() {
		grid = UnobservableGridFactory.get().emptyGrid(N, N, UNVISITED);
		new IterativeDFS(grid).createMaze(0, 0);
		grid.setDefaultEdgeLabel((u, v) -> 1);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		AStarSearch astar = new AStarSearch(grid, (u, v) -> 1, (u, v) -> grid.manhattan(u, v));
		assertState(grid.vertices(), astar::getState, UNVISITED);
		astar.findPath(source, target);
		assertTrue(astar.getState(target) == COMPLETED);
		assertTrue(astar.getParent(target) != -1);
		assertTrue(astar.getCost(target) != -1);
	}
}