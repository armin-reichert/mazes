package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.curves.CurveUtils.traverse;
import static de.amr.easy.grid.impl.Topologies.TOP4;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.curves.Curve;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.curves.HilbertLCurveWirth;
import de.amr.easy.grid.curves.MooreLCurve;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.Grid;

public class GridTraversalTests {

	private static final int LOG_N = 9;
	private static final int SIZE = 1 << LOG_N;

	private Grid2D<TraversalState, Integer> squareGrid;

	@Before
	public void setUp() {
		squareGrid = new Grid<>(SIZE, SIZE, TOP4, UNVISITED);
		squareGrid.fill();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testBFS() {
		BreadthFirstTraversal<WeightedEdge<Integer>> bfs = new BreadthFirstTraversal<>(squareGrid, squareGrid.cell(CENTER));
		squareGrid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == UNVISITED);
		});
		bfs.traverseGraph();
		squareGrid.vertexStream().forEach(cell -> {
			assertTrue(bfs.getState(cell) == COMPLETED);
		});
	}

	@Test
	public void testDFS() {
		Integer source = squareGrid.cell(TOP_LEFT), target = squareGrid.cell(BOTTOM_RIGHT);
		DepthFirstTraversal<WeightedEdge<Integer>> dfs = new DepthFirstTraversal<>(squareGrid, source, target);
		squareGrid.vertexStream().forEach(cell -> {
			assertTrue(dfs.getState(cell) == UNVISITED);
		});
		dfs.traverseGraph();
		assertTrue(dfs.getState(source) == COMPLETED || dfs.getState(target) == COMPLETED);
		for (Integer cell : dfs.findPath(target)) {
			assertTrue(dfs.getState(cell) == COMPLETED);
		}
	}

	@Test
	public void testHilbertCurve() {
		Curve curve = new HilbertCurve(LOG_N);
		traverse(curve, squareGrid, squareGrid.cell(GridPosition.TOP_RIGHT), (from, to) -> {
			squareGrid.set(from, COMPLETED);
			squareGrid.set(to, COMPLETED);
		});
		squareGrid.vertexStream().forEach(cell -> assertTrue(squareGrid.get(cell) == COMPLETED));
	}

	@Test
	public void testHilbertLCurve() {
		Curve curve = new HilbertLCurve(LOG_N);
		traverse(curve, squareGrid, squareGrid.cell(GridPosition.BOTTOM_LEFT), (from, to) -> {
			squareGrid.set(from, COMPLETED);
			squareGrid.set(to, COMPLETED);
		});
		squareGrid.vertexStream().forEach(cell -> assertTrue(squareGrid.get(cell) == COMPLETED));
	}

	@Test
	public void testHilbertLCurveWirth() {
		Curve curve = new HilbertLCurveWirth(LOG_N);
		traverse(curve, squareGrid, squareGrid.cell(GridPosition.TOP_RIGHT), (from, to) -> {
			squareGrid.set(from, COMPLETED);
			squareGrid.set(to, COMPLETED);
		});
		squareGrid.vertexStream().forEach(cell -> assertTrue(squareGrid.get(cell) == COMPLETED));
	}

	@Test
	public void testMooreLCurve() {
		Curve curve = new MooreLCurve(LOG_N);
		int startCol = SIZE / 2, startRow = SIZE - 1;
		int startCell = squareGrid.cell(startCol, startRow);
		traverse(curve, squareGrid, startCell, (from, to) -> {
			squareGrid.set(from, COMPLETED);
			squareGrid.set(to, COMPLETED);
		});
		squareGrid.vertexStream().forEach(cell -> assertTrue(squareGrid.get(cell) == COMPLETED));
	}

	@Test
	public void testPeanoCurve() {
		Grid2D<TraversalState, Integer> grid = new Grid<>(243, 243, TOP4, UNVISITED);
		Curve curve = new PeanoCurve(5);
		traverse(curve, grid, grid.cell(GridPosition.BOTTOM_LEFT), (from, to) -> {
			grid.set(from, COMPLETED);
			grid.set(to, COMPLETED);
		});
		grid.vertexStream().forEach(cell -> assertTrue(grid.get(cell) == COMPLETED));
	}
}
