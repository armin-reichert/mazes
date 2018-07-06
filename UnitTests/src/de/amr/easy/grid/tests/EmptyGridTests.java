package de.amr.easy.grid.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.UndirectedEdge;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.Top4;

public class EmptyGridTests {

	private GridGraph<Void, Void> grid;

	@Before
	public void setUp() {
		grid = new GridGraph<>(0, 0, Top4.get(), null, (u, v) -> null, UndirectedEdge::new);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGridSize() {
		assertEquals(grid.numEdges(), 0);
		assertEquals(grid.numVertices(), 0);
		assertEquals(grid.numCols(), 0);
		assertEquals(grid.numRows(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridAccessException() {
		grid.cell(0, 0);
	}

	@Test
	public void testGridEdgeStream() {
		assertTrue(grid.edges().count() == 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridEdgeAccess() {
		grid.edge(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridEdgeAdd() {
		grid.addEdge(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGridVertexDegree() {
		grid.degree(0);
	}
}