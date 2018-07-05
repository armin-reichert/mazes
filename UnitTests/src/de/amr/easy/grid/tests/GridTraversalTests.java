package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.curves.CurveUtils.cells;
import static de.amr.easy.grid.curves.CurveUtils.traverse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.impl.traversal.HillClimbing;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.curves.HilbertLCurveWirth;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.maze.alg.core.OrthogonalGrid;
import de.amr.easy.maze.alg.traversal.IterativeDFS;
import de.amr.easy.util.StreamUtils;

public class GridTraversalTests {

	private static final int K = 8;
	private static final int N = 1 << K; // N = 2^K

	private static void assertState(IntStream cells, Function<Integer, TraversalState> getState,
			TraversalState expected) {
		cells.forEach(cell -> assertTrue(getState.apply(cell) == expected));
	}

	private OrthogonalGrid grid;

	@Before
	public void setUp() {
		grid = OrthogonalGrid.fullGrid(N, N, UNVISITED);
	}

	@After
	public void tearDown() {
	}

	private void assertAllCells(TraversalState state) {
		grid.vertices().forEach(cell -> assertTrue(grid.get(cell) == state));
	}

	private void setCompleted(int from, int to) {
		grid.set(from, COMPLETED);
		grid.set(to, COMPLETED);
	}

	@Test
	public void testBFS() {
		BreadthFirstTraversal<TraversalState, ?> bfs = new BreadthFirstTraversal<>(grid);
		assertState(grid.vertices(), bfs::getState, UNVISITED);
		bfs.traverseGraph(grid.cell(CENTER));
		assertState(grid.vertices(), bfs::getState, COMPLETED);
	}

	@Test
	public void testBestFS() {
		grid = new IterativeDFS(N, N).createMaze(0, 0);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		BestFirstTraversal<?, ?, Integer> best = new BestFirstTraversal<>(grid, x -> grid.manhattan(x, target));
		assertState(grid.vertices(), best::getState, UNVISITED);
		best.traverseGraph(source);
		assertState(grid.vertices(), best::getState, COMPLETED);
		best.traverseGraph(source, target);
	}

	@Test
	public void testAStar() {
		grid = new IterativeDFS(N, N).createMaze(0, 0);
		grid.setDefaultEdgeLabel((u, v) -> 1);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		AStarTraversal<TraversalState> astar = new AStarTraversal<>(grid, (u, v) -> grid.manhattan(u, v));
		assertState(grid.vertices(), astar::getState, UNVISITED);
		astar.traverseGraph(source, target);
		assertTrue(astar.getState(target) == VISITED);
		assertTrue(astar.getParent(target) != -1);
		assertTrue(astar.getDistFromSource(target) != -1);
	}

	@Test
	public void testDFS() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal dfs = new DepthFirstTraversal(grid);
		assertState(grid.vertices(), dfs::getState, UNVISITED);
		dfs.traverseGraph(source, target);
		assertState(StreamUtils.toIntStream(dfs.path(target)), dfs::getState, VISITED);
	}

	@Test
	public void testDFS2() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal2 dfs = new DepthFirstTraversal2(grid);
		assertState(grid.vertices(), dfs::getState, UNVISITED);
		dfs.traverseGraph(source, target);
		assertState(StreamUtils.toIntStream(dfs.path(target)), dfs::getState, COMPLETED);
	}

	@Test
	public void testHillClimbing() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		Function<Integer, Integer> cost = u -> grid.manhattan(u, target);
		HillClimbing<Integer> hillClimbing = new HillClimbing<>(grid, cost);
		assertState(grid.vertices(), hillClimbing::getState, UNVISITED);
		hillClimbing.traverseGraph(source, target);
		hillClimbing.path(target).forEach(cell -> assertTrue(hillClimbing.getState(cell) != UNVISITED));
	}

	@Test
	public void testHilbertCurve() {
		assertAllCells(UNVISITED);
		traverse(new HilbertCurve(K), grid, grid.cell(TOP_RIGHT), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testHilbertLCurve() {
		assertAllCells(UNVISITED);
		traverse(new HilbertLCurve(K), grid, grid.cell(BOTTOM_LEFT), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testHilbertLCurveWirth() {
		assertAllCells(UNVISITED);
		traverse(new HilbertLCurveWirth(K), grid, grid.cell(TOP_RIGHT), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testMooreLCurve() {
		assertAllCells(UNVISITED);
		traverse(new MooreLCurve(K), grid, grid.cell(N / 2, N - 1), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testPeanoCurve() {
		grid = OrthogonalGrid.emptyGrid(243, 243, UNVISITED);
		assertAllCells(UNVISITED);
		traverse(new PeanoCurve(5), grid, grid.cell(BOTTOM_LEFT), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testCurveStream() {
		cells(new HilbertCurve(K), grid, grid.cell(TOP_RIGHT)).forEach(cell -> grid.set(cell, COMPLETED));
		assertAllCells(COMPLETED);
	}
}