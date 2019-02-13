package de.amr.easy.grid.tests;

import static de.amr.graph.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.graph.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.graph.grid.curves.CurveUtils.cells;
import static de.amr.graph.grid.curves.CurveUtils.traverse;
import static de.amr.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.graph.pathfinder.api.TraversalState.UNVISITED;
import static de.amr.graph.pathfinder.api.TraversalState.VISITED;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.datastruct.StreamUtils;
import de.amr.graph.grid.curves.HilbertCurve;
import de.amr.graph.grid.curves.HilbertLCurve;
import de.amr.graph.grid.curves.HilbertLCurveWirth;
import de.amr.graph.grid.curves.MooreLCurve;
import de.amr.graph.grid.curves.PeanoCurve;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.pathfinder.api.TraversalState;
import de.amr.graph.pathfinder.impl.AStarSearch;
import de.amr.graph.pathfinder.impl.BestFirstSearch;
import de.amr.graph.pathfinder.impl.BreadthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch2;
import de.amr.graph.pathfinder.impl.HillClimbingSearch;
import de.amr.maze.alg.traversal.IterativeDFS;

public class GridTraversalTest {

	private static final int K = 8;
	private static final int N = 1 << K; // N = 2^K

	private static void assertState(IntStream cells, Function<Integer, TraversalState> fnSupplyState,
			TraversalState... expected) {
		cells.forEach(cell -> assertTrue(Arrays.stream(expected).anyMatch(s -> s == fnSupplyState.apply(cell))));
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
		BreadthFirstSearch<TraversalState, ?> bfs = new BreadthFirstSearch<>(grid);
		assertState(grid.vertices(), bfs::getState, UNVISITED);
		bfs.traverseGraph(grid.cell(CENTER));
		assertState(grid.vertices(), bfs::getState, VISITED, COMPLETED);
	}

	@Test
	public void testBestFS() {
		grid = new IterativeDFS(N, N).createMaze(0, 0);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		BestFirstSearch<?, ?> best = new BestFirstSearch<>(grid, x -> grid.manhattan(x, target));
		assertState(grid.vertices(), best::getState, UNVISITED);
		best.traverseGraph(source);
		assertState(grid.vertices(), best::getState, VISITED, COMPLETED);
		best.traverseGraph(source, target);
	}

	@Test
	public void testAStar() {
		grid = new IterativeDFS(N, N).createMaze(0, 0);
		grid.setDefaultEdgeLabel((u, v) -> 1);
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		AStarSearch<TraversalState, Integer> astar = new AStarSearch<>(grid, edge -> 1,
				(u, v) -> grid.manhattan(u, v));
		assertState(grid.vertices(), astar::getState, UNVISITED);
		astar.traverseGraph(source, target);
		assertTrue(astar.isClosed(target));
		assertTrue(astar.getParent(target) != -1);
		assertTrue(astar.getCost(target) != -1);
	}

	@Test
	public void testDFS() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstSearch<?, ?> dfs = new DepthFirstSearch<>(grid);
		assertState(grid.vertices(), dfs::getState, UNVISITED);
		assertState(StreamUtils.toIntStream(dfs.path(source, target)), dfs::getState, VISITED, COMPLETED);
	}

	@Test
	public void testDFS2() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstSearch2<?, ?> dfs = new DepthFirstSearch2<>(grid);
		assertState(grid.vertices(), dfs::getState, UNVISITED);
		assertState(StreamUtils.toIntStream(dfs.path(source, target)), dfs::getState, COMPLETED);
	}

	@Test
	public void testHillClimbing() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		ToDoubleFunction<Integer> cost = u -> grid.manhattan(u, target);
		HillClimbingSearch<?, ?> hillClimbing = new HillClimbingSearch<>(grid, cost);
		assertState(grid.vertices(), hillClimbing::getState, UNVISITED);
		hillClimbing.path(source, target).forEach(cell -> assertTrue(hillClimbing.getState(cell) != UNVISITED));
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