package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.Dir4.E;
import static de.amr.easy.grid.api.Dir4.N;
import static de.amr.easy.grid.api.Dir4.S;
import static de.amr.easy.grid.api.Dir4.W;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.alg.CycleChecker;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.NakedGrid;
import de.amr.easy.maze.alg.RandomBFS;

/**
 * Test case for {@link NakedGrid}
 * 
 * @author Armin Reichert
 *
 */
public class GridTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private Grid2D<Dir4, TraversalState, Integer> grid;

	@Before
	public void setUp() {
		grid = new Grid<>(WIDTH, HEIGHT, UNVISITED);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGridInitialization() {
		assertEquals(grid.edgeCount(), 0);
		assertEquals(grid.vertexCount(), WIDTH * HEIGHT);
		assertEquals(grid.numCols(), WIDTH);
		assertEquals(grid.numRows(), HEIGHT);
		assertEquals(grid.numCells(), grid.vertexStream().count());
		assertEquals(grid.edgeCount(), grid.edgeStream().count());
	}

	@Test
	public void testInitialContent() {
		assertEquals(grid.vertexStream().filter(cell -> grid.get(cell) == UNVISITED).count(), grid.numCells());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddVertexThrowsException() {
		grid.addVertex(0);
	}

	@Test
	public void testGetNonexistingEdge() {
		assertFalse(grid.edge(0, 1).isPresent());
	}

	@Test
	public void testAddEdge() {
		int numEdges = grid.edgeCount();
		assert (!grid.edge(0, 1).isPresent());
		grid.addEdge(0, 1);
		assertEquals(numEdges + 1, grid.edgeCount());
	}

	@Test(expected = IllegalStateException.class)
	public void addEdgeTwiceThrowsException() {
		grid.addEdge(0, 1);
		grid.addEdge(0, 1);
	}

	@Test(expected = IllegalStateException.class)
	public void addEdgeToNonNeighborThrowsException() {
		grid.addEdge(0, 2);
	}

	@Test
	public void testFillAllEdges() {
		assertEquals(grid.edgeCount(), 0);
		grid.makeFullGrid();
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
	}

	@Test
	public void testRemoveEdge() {
		int numEdges = grid.edgeCount();
		grid.addEdge(0, 1);
		assertEquals(grid.edgeCount(), numEdges + 1);
		grid.removeEdge(0, 1);
		assertEquals(grid.edgeCount(), numEdges);
		assertFalse(grid.edge(0, 1).isPresent());
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveEdgeTwiceThrowsException() {
		grid.addEdge(0, 1);
		grid.removeEdge(0, 1);
		grid.removeEdge(0, 1);
	}

	@Test
	public void testRemoveAllEdges() {
		assertEquals(grid.edgeCount(), 0);
		grid.makeFullGrid();
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
		grid.removeEdges();
		assertEquals(grid.edgeCount(), 0);
	}

	@Test
	public void testAdjVertices() {
		assertFalse(grid.adjacent(0, 1));
		grid.addEdge(0, 1);
		assertTrue(grid.adjacent(0, 1));
		grid.removeEdge(0, 1);
		assertFalse(grid.adjacent(0, 1));
	}

	@Test
	public void testCellCoordinates() {
		for (int x = 0; x < grid.numCols(); ++x) {
			for (int y = 0; y < grid.numRows(); ++y) {
				Integer cell = grid.cell(x, y);
				assertEquals(grid.col(cell), x);
				assertEquals(grid.row(cell), y);
			}
		}
	}

	@Test
	public void testGetNeighbor() {
		for (int x = 0; x < grid.numCols(); ++x) {
			for (int y = 0; y < grid.numRows(); ++y) {
				Integer cell = grid.cell(x, y);
				if (y > 0) {
					Integer n = grid.neighbor(cell, N).get();
					assertEquals(n, grid.cell(x, y - 1));
				}
				if (x < grid.numCols() - 1) {
					Integer e = grid.neighbor(cell, E).get();
					assertEquals(e, grid.cell(x + 1, y));
				}
				if (y < grid.numRows() - 1) {
					Integer s = grid.neighbor(cell, S).get();
					assertEquals(s, grid.cell(x, y + 1));
				}
				if (x > 0) {
					Integer w = grid.neighbor(cell, W).get();
					assertEquals(w, grid.cell(x - 1, y));
				}
			}
		}
	}

	@Test
	public void testCycleCheckerSquare() {
		CycleChecker<Integer, WeightedEdge<Integer, Integer>> cycleChecker = new CycleChecker<>();
		// create graph without cycle:
		Integer a = grid.cell(0, 0);
		Integer b = grid.cell(1, 0);
		Integer c = grid.cell(1, 1);
		Integer d = grid.cell(0, 1);
		grid.addEdge(a, b);
		grid.addEdge(b, c);
		grid.addEdge(c, d);
		assertFalse(cycleChecker.test(grid));
		// add edge to create cycle:
		grid.addEdge(d, a);
		assertTrue(cycleChecker.test(grid));
	}

	@Test
	public void testCycleCheckerSpanningTree() {
		CycleChecker<Integer, WeightedEdge<Integer, Integer>> cycleChecker = new CycleChecker<>();
		// create a spanning tree
		new RandomBFS(grid).accept(grid.cell(0, 0));
		assertFalse(cycleChecker.test(grid));
		// add edge at first vertex that has not full degree:
		/*@formatter:off*/
		grid.vertexStream()
			.filter(cell -> grid.degree(cell) < grid.neighbors(cell).count())
			.findFirst()
			.ifPresent(cell -> {
				for (Dir4 dir : Dir4.values()) {
					Optional<Integer> neighbor = grid.neighbor(cell, dir);
					if (neighbor.isPresent() && !grid.adjacent(cell, neighbor.get())) {
						grid.addEdge(cell, neighbor.get());
						break;
					}
				}
			});
		/*@formatter:on*/
		// now there must be a cycle
		assertTrue(cycleChecker.test(grid));
	}
}
