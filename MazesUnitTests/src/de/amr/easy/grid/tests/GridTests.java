package de.amr.easy.grid.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.BareGrid;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.traversal.RandomBFS;
import de.amr.easy.util.GraphUtils;

/**
 * Test case for {@link BareGrid}
 * 
 * @author Armin Reichert
 *
 */
public class GridTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private Grid2D<TraversalState, Integer> grid;
	private Random rnd = new Random();

	private TraversalState randomTraversalState() {
		TraversalState values[] = TraversalState.values();
		return values[rnd.nextInt(values.length)];
	}

	@Before
	public void setUp() {
		grid = new Grid<>(WIDTH, HEIGHT, Top4.get(), UNVISITED, false);
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

	@Test
	public void testGridCopyConstructor() {
		Grid<TraversalState, Integer> copy = new Grid<>(grid);
		assertEquals(grid.edgeCount(), copy.edgeCount());
		assertEquals(grid.vertexCount(), copy.vertexCount());
		assertEquals(grid.numCols(), copy.numCols());
		assertEquals(grid.numRows(), copy.numRows());
		assertEquals(grid.getTopology(), copy.getTopology());
		assertEquals(grid.getDefaultContent(), copy.getDefaultContent());
		grid.vertexStream().forEach(v -> assertEquals(grid.get(v), copy.get(v)));
	}

	@Test
	public void testGridCopyRandomContent() {
		grid.vertexStream().forEach(v -> grid.set(v, randomTraversalState()));
		Grid<TraversalState, Integer> copy = new Grid<>(grid);
		grid.vertexStream().forEach(v -> assertEquals(grid.get(v), copy.get(v)));
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
	public void testAreNeighbors() {
		int center = grid.cell(CENTER);
		assertTrue(grid.areNeighbors(center, center + 1));
		assertTrue(grid.areNeighbors(center, center - 1));
		assertTrue(grid.areNeighbors(center, center - grid.numCols()));
		assertTrue(grid.areNeighbors(center, center + grid.numCols()));
		assertTrue(!grid.areNeighbors(center, center));
		assertTrue(!grid.areNeighbors(center, center - 2));
		assertTrue(!grid.areNeighbors(center, center + 2));
	}

	@Test
	public void testFillAllEdges() {
		assertEquals(grid.edgeCount(), 0);
		grid.fill();
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
		grid.fill();
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
		grid.removeEdges();
		assertEquals(grid.edgeCount(), 0);
	}

	@Test
	public void testAdjVertices() {
		assertFalse(grid.adjacent(0, 1));
		assertFalse(grid.adjacent(1, 0));
		grid.addEdge(0, 1);
		assertTrue(grid.adjacent(0, 1));
		assertTrue(grid.adjacent(1, 0));
		grid.removeEdge(0, 1);
		assertFalse(grid.adjacent(0, 1));
		assertFalse(grid.adjacent(1, 0));
		grid.addEdge(1, 0);
		assertTrue(grid.adjacent(1, 0));
		assertTrue(grid.adjacent(0, 1));
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
					int n = grid.neighbor(cell, N).getAsInt();
					assertEquals(n, grid.cell(x, y - 1));
				}
				if (x < grid.numCols() - 1) {
					int e = grid.neighbor(cell, E).getAsInt();
					assertEquals(e, grid.cell(x + 1, y));
				}
				if (y < grid.numRows() - 1) {
					int s = grid.neighbor(cell, S).getAsInt();
					assertEquals(s, grid.cell(x, y + 1));
				}
				if (x > 0) {
					int w = grid.neighbor(cell, W).getAsInt();
					assertEquals(w, grid.cell(x - 1, y));
				}
			}
		}
	}

	@Test
	public void testCycleCheckerSquare() {
		// create graph without cycle:
		Integer a = grid.cell(0, 0);
		Integer b = grid.cell(1, 0);
		Integer c = grid.cell(1, 1);
		Integer d = grid.cell(0, 1);
		grid.addEdge(a, b);
		grid.addEdge(b, c);
		grid.addEdge(c, d);
		assertFalse(GraphUtils.containsCycle(grid));
		// add edge to create cycle:
		grid.addEdge(d, a);
		assertTrue(GraphUtils.containsCycle(grid));
	}

	@Test
	public void testCycleCheckerSpanningTree() {
		// create a spanning tree
		new RandomBFS(grid).run(grid.cell(0, 0));
		assertFalse(GraphUtils.containsCycle(grid));

		// Find vertex with non-adjacent neighbor. Adding an edge to this neighbor produces a cycle.
		///*@formatter:off*/
		grid.vertexStream()
			.filter(cell -> grid.neighbors(cell).anyMatch(neighbor -> !grid.adjacent(cell, neighbor)))
			.findAny()
			.ifPresent(cell -> 	
				grid.neighbors(cell)
					.filter(neighbor -> !grid.adjacent(cell, neighbor))
					.findAny()
					.ifPresent(neighbor -> grid.addEdge(cell, neighbor)));
		///*@formatter:on*/

		// now there must be a cycle
		assertTrue(GraphUtils.containsCycle(grid));
	}

	@Test
	public void testConnected() {
		int u = grid.cell(GridPosition.TOP_LEFT);
		int v = grid.cell(GridPosition.BOTTOM_RIGHT);
		assertFalse(GraphUtils.areConnected(grid, u, v));
		grid.fill();
		assertTrue(GraphUtils.areConnected(grid, u, v));
		grid.removeEdges();

		assertFalse(GraphUtils.areConnected(grid, 0, 1));
		grid.addEdge(0, 1);
		assertTrue(GraphUtils.areConnected(grid, 0, 1));
		grid.removeEdge(0, 1);
		assertFalse(GraphUtils.areConnected(grid, 0, 1));

		assertTrue(GraphUtils.areConnected(grid, 0, 0));
	}
}
