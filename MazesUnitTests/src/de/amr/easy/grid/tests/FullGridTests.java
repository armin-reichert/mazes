package de.amr.easy.grid.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.impl.NakedGrid;

public class FullGridTests {

	private static final int WIDTH = 15;
	private static final int HEIGHT = 10;

	private NakedGrid2D<?> grid;

	@Before
	public void setUp() {
		grid = new NakedGrid<>(WIDTH, HEIGHT).makeFullGrid();
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGridSize() {
		assertEquals(grid.edgeCount(), 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
		assertEquals(grid.vertexCount(), WIDTH * HEIGHT);
		assertEquals(grid.numCols(), WIDTH);
		assertEquals(grid.numRows(), HEIGHT);
	}

	@Test
	public void testGridEdgeStream() {
		assertTrue(grid.edgeStream().count() == 2 * WIDTH * HEIGHT - (WIDTH + HEIGHT));
	}

	private void assertContainsExactly(Stream<Integer> stream, Integer... cells) {
		Set<Integer> set = new HashSet<>();
		stream.forEach(cell -> {
			set.add(cell);
		});
		for (Integer cell : cells) {
			assertTrue(set.contains(cell));
		}
		assertEquals(set.size(), cells.length);
	}

	@Test
	public void testAdjVertices() {
		Integer cell = grid.cell(1, 1);
		assertContainsExactly(grid.adjVertices(cell), grid.cell(1, 0), grid.cell(1, 2), grid.cell(2, 1), grid.cell(0, 1));
	}

	@Test
	public void testAdjVerticesAtCorners() {
		Integer cell;
		cell = grid.cell(GridPosition.TOP_LEFT);
		assertContainsExactly(grid.adjVertices(cell), grid.cell(1, 0), grid.cell(0, 1));
		cell = grid.cell(GridPosition.TOP_RIGHT);
		assertContainsExactly(grid.adjVertices(cell), grid.cell(WIDTH - 2, 0), grid.cell(WIDTH - 1, 1));
		cell = grid.cell(GridPosition.BOTTOM_LEFT);
		assertContainsExactly(grid.adjVertices(cell), grid.cell(0, HEIGHT - 2), grid.cell(1, HEIGHT - 1));
		cell = grid.cell(GridPosition.BOTTOM_RIGHT);
		assertContainsExactly(grid.adjVertices(cell), grid.cell(WIDTH - 1, HEIGHT - 2), grid.cell(WIDTH - 2, HEIGHT - 1));
	}

	@Test
	public void testDegree() {
		assertEquals(grid.degree(grid.cell(GridPosition.TOP_LEFT)), 2);
		assertEquals(grid.degree(grid.cell(GridPosition.TOP_RIGHT)), 2);
		assertEquals(grid.degree(grid.cell(GridPosition.BOTTOM_LEFT)), 2);
		assertEquals(grid.degree(grid.cell(GridPosition.BOTTOM_RIGHT)), 2);
		for (int x = 0; x < grid.numCols(); ++x) {
			for (int y = 0; y < grid.numRows(); ++y) {
				Integer cell = grid.cell(x, y);
				assertTrue(grid.degree(cell) >= 2 && grid.degree(cell) <= 4);
				if (x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1) {
					assertTrue(grid.degree(cell) <= 3);
				}
			}
		}
	}

	@Test
	public void testConnectedTowards() {
		for (int x = 0; x < grid.numCols(); ++x) {
			for (int y = 0; y < grid.numRows(); ++y) {
				Integer cell = grid.cell(x, y);
				if (grid.numCols() > 1) {
					if (x == 0) {
						assertTrue(grid.isConnected(cell, Direction.E));
					}
					if (x == grid.numCols() - 1) {
						assertTrue(grid.isConnected(cell, Direction.W));
					}
				}
				if (grid.numRows() > 1) {
					if (y == 0) {
						assertTrue(grid.isConnected(cell, Direction.S));
					}
					if (y == grid.numRows() - 1) {
						assertTrue(grid.isConnected(cell, Direction.N));
					}
				}
			}
		}
	}

	@Test
	public void testEdgesPermuted() {
		assertTrue(grid.fullGridEdgesPermuted().count() == grid.edgeStream().count());
	}
}
