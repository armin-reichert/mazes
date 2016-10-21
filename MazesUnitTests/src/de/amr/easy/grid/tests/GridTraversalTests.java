package de.amr.easy.grid.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.grid.api.NakedGrid2D;
import de.amr.easy.grid.impl.NakedGrid;

public class GridTraversalTests {

	private static final int WIDTH = 3;
	private static final int HEIGHT = 2;

	private NakedGrid2D grid;

	@Before
	public void setUp() {
		grid = new NakedGrid(WIDTH, HEIGHT);
		grid.makeFullGrid();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testBFS() {
	}
}
