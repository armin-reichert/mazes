package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.curves.CurveUtils.traverse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Curve;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.curves.HilbertLCurveWirth;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;

public class GridTraversalTests {

	private static final int K = 8;
	private static final int N = 1 << K; // N = 2^K

	private Grid2D<TraversalState, Integer> grid;

	@Before
	public void setUp() {
		grid = new Grid<>(N, N, Top4.get(), UNVISITED);
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
		BreadthFirstTraversal<WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(grid, grid.cell(CENTER));
		grid.vertexStream().forEach(cell -> assertTrue(bfs.getState(cell) == UNVISITED));
		bfs.traverseGraph();
		grid.vertexStream().forEach(cell -> assertTrue(bfs.getState(cell) == COMPLETED));
	}

	@Test
	public void testDFS() {
		int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal<WeightedEdge<Integer>> dfs = new DepthFirstTraversal<>(grid, source, target);
		grid.vertexStream().forEach(cell -> assertTrue(dfs.getState(cell) == UNVISITED));
		dfs.traverseGraph();
		dfs.findPath(target).forEach(cell -> assertTrue(dfs.getState(cell) == COMPLETED));
	}

	@Test
	public void testHilbertCurve() {
		assertAllCells(UNVISITED);
		traverse(new HilbertCurve(K), grid, grid.cell(TOP_RIGHT), this::setCompleted);
		assertAllCells(COMPLETED);
	}

	@Test
	public void testHilbertLCurve() {
		Curve curve = new HilbertLCurve(K);
		assertAllCells(UNVISITED);
		traverse(curve, grid, grid.cell(BOTTOM_LEFT), this::setCompleted);
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
		grid = new Grid<>(243, 243, Top4.get(), UNVISITED);
		assertAllCells(UNVISITED);
		traverse(new PeanoCurve(5), grid, grid.cell(BOTTOM_LEFT), this::setCompleted);
		assertAllCells(COMPLETED);
	}
}