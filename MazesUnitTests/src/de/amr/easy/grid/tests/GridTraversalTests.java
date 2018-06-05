package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
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

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.curves.HilbertLCurveWirth;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

public class GridTraversalTests {

	private static final int K = 8;
	private static final int N = 1 << K; // N = 2^K

	private static void assertState(IntStream cells, Function<Integer, TraversalState> getState,
			TraversalState expected) {
		cells.forEach(cell -> assertTrue(getState.apply(cell) == expected));
	}

	private Grid2D<TraversalState, Integer> grid;

	@Before
	public void setUp() {
		grid = new Grid<>(N, N, Top4.get(), UNVISITED, false);
		grid.fill();
	}

	@After
	public void tearDown() {
	}

	private void assertAllCells(TraversalState state) {
		grid.vertexStream().forEach(cell -> assertTrue(grid.get(cell) == state));
	}

	private void setCompleted(int from, int to) {
		grid.set(from, COMPLETED);
		grid.set(to, COMPLETED);
	}

	@Test
	public void testBFS() {
		BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid);
		assertState(grid.vertexStream(), bfs::getState, UNVISITED);
		bfs.traverseGraph(grid.cell(CENTER));
		assertState(grid.vertexStream(), bfs::getState, COMPLETED);
	}

	@Test
	public void testBestFS() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		grid.removeEdges();
		new IterativeDFS(grid).run(target);
		BestFirstTraversal best = new BestFirstTraversal(grid,
				(u, v) -> Integer.compare(grid.manhattan(u, target), grid.manhattan(v, target)));
		assertState(grid.vertexStream(), best::getState, UNVISITED);
		best.traverseGraph(source);
		assertState(grid.vertexStream(), best::getState, COMPLETED);
		best.traverseGraph(source, target);
		long length = best.findPath(target).count();
		System.out.println("Best-first search found path of length " + length);
	}

	@Test
	public void testDFS() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal dfs = new DepthFirstTraversal(grid);
		assertState(grid.vertexStream(), dfs::getState, UNVISITED);
		dfs.traverseGraph(source, target);
		assertState(dfs.findPath(target), dfs::getState, VISITED);
	}

	@Test
	public void testDFS2() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal2 dfs = new DepthFirstTraversal2(grid);
		assertState(grid.vertexStream(), dfs::getState, UNVISITED);
		dfs.traverseGraph(source, target);
		assertState(dfs.findPath(target), dfs::getState, COMPLETED);
	}

	@Test
	public void testHillClimbing() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		Function<Integer, Integer> cost = u -> grid.manhattan(u, target);
		HillClimbing<Integer> hillClimbing = new HillClimbing<>(grid, cost);
		assertState(grid.vertexStream(), hillClimbing::getState, UNVISITED);
		hillClimbing.traverseGraph(source, target);
		IntStream path = hillClimbing.findPath(target);
		path.forEach(cell -> assertTrue(hillClimbing.getState(cell) != UNVISITED));
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
		grid = new Grid<>(243, 243, Top4.get(), UNVISITED, false);
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