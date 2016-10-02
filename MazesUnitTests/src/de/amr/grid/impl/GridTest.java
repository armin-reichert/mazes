package de.amr.grid.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.RawGrid;

/**
 * Test case for {@link RawGrid}
 * 
 * @author Armin Reichert
 *
 */
public class GridTest {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;

	private Grid2D<Integer, DefaultEdge<Integer>> grid;

	@Before
	public void setUp() {
		grid = new RawGrid(WIDTH, HEIGHT);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGridSize() {
		assertEquals(grid.edgeCount(), 0);
		assertEquals(grid.vertexCount(), WIDTH * HEIGHT);
		assertEquals(grid.numCols(), WIDTH);
		assertEquals(grid.numRows(), HEIGHT);
	}

	@Test
	public void testGridVertexIterator() {
		Iterator<Integer> vertexIterator = grid.vertexSequence().iterator();
		for (int i = 0; i < grid.vertexCount(); ++i) {
			assertTrue(vertexIterator.hasNext());
			vertexIterator.next();
		}
		assertFalse(vertexIterator.hasNext());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddVertexThrowsException() {
		grid.addVertex(0);
	}

	@Test
	public void testGridEdgeIterator() {
		Iterator<DefaultEdge<Integer>> edgeIterator = grid.edgeSequence().iterator();
		assertFalse(edgeIterator.hasNext());
	}

	@Test
	public void testGetNonexistingEdge() {
		assertNull(grid.edge(0, 1));
	}

	@Test
	public void testGetExistingEdge() {
		DefaultEdge<Integer> edge = new DefaultEdge<Integer>(0, 1);
		grid.addEdge(edge);
		assertEquals(edge, grid.edge(0, 1));
	}

	@Test
	public void testAddEdge() {
		int numEdges = grid.edgeCount();
		assertNull(grid.edge(0, 1));
		DefaultEdge<Integer> edge = new DefaultEdge<Integer>(0, 1);
		grid.addEdge(edge);
		assertEquals(numEdges + 1, grid.edgeCount());
		assertEquals(edge, grid.edge(0, 1));
	}

	@Test(expected = IllegalStateException.class)
	public void addEdgeTwiceThrowsException() {
		grid.addEdge(new DefaultEdge<Integer>(0, 1));
		grid.addEdge(new DefaultEdge<Integer>(0, 1));
	}

	@Test
	public void testFillAllEdges() {
		assertEquals(grid.edgeCount(), 0);
		grid.fillAllEdges();
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
	}

	@Test
	public void testRemoveEdge() {
		int numEdges = grid.edgeCount();
		DefaultEdge<Integer> edge = new DefaultEdge<Integer>(0, 1);
		grid.addEdge(edge);
		assertEquals(grid.edgeCount(), numEdges + 1);
		grid.removeEdge(edge);
		assertEquals(grid.edgeCount(), numEdges);
		assertNull(grid.edge(0, 1));
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveEdgeTwiceThrowsException() {
		grid.addEdge(new DefaultEdge<Integer>(0, 1));
		grid.removeEdge(new DefaultEdge<Integer>(0, 1));
		grid.removeEdge(new DefaultEdge<Integer>(0, 1));
	}

	@Test
	public void testRemoveAllEdges() {
		assertEquals(grid.edgeCount(), 0);
		grid.fillAllEdges();
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
		grid.removeEdges();
		assertEquals(grid.edgeCount(), 0);
	}

	@Test
	public void testAdjVertices() {
		assertFalse(grid.adjacent(0, 1));
		DefaultEdge<Integer> edge = new DefaultEdge<Integer>(0, 1);
		grid.addEdge(edge);
		assertTrue(grid.adjacent(0, 1));
		grid.removeEdge(edge);
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
					Integer n = grid.neighbor(cell, Direction.N);
					assertEquals(n, grid.cell(x, y - 1));
				}
				if (x < grid.numCols() - 1) {
					Integer e = grid.neighbor(cell, Direction.E);
					assertEquals(e, grid.cell(x + 1, y));
				}
				if (y < grid.numRows() - 1) {
					Integer s = grid.neighbor(cell, Direction.S);
					assertEquals(s, grid.cell(x, y + 1));
				}
				if (x > 0) {
					Integer w = grid.neighbor(cell, Direction.W);
					assertEquals(w, grid.cell(x - 1, y));
				}
			}
		}
	}

}
