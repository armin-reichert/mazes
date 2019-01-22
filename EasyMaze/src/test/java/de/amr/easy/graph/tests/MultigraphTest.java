package de.amr.easy.graph.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.amr.easy.graph.core.api.Multigraph;
import de.amr.easy.graph.core.api.UndirectedEdge;
import de.amr.easy.graph.core.impl.DefaultMultigraph;
import de.amr.easy.graph.util.GraphUtils;

public class MultigraphTest {

	@Test
	public void testCreation() {
		Multigraph g = new DefaultMultigraph();
		assertEquals(0, g.vertexCount());
		assertEquals(0, g.edgeCount());
		assertEquals(0, g.vertexStream().count());
		assertEquals(0, g.edgeStream().count());
	}

	@Test
	public void testAddSingleVertex() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		assertEquals(1, g.vertexCount());
		assertTrue(g.vertexStream().findFirst().isPresent());
		assertTrue(g.vertexStream().findFirst().getAsInt() == 42);
	}

	@Test
	public void testAddSingleEdge() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);
		assertEquals(2, g.vertexCount());
		g.addEdge(new UndirectedEdge(42, 43));
		assertEquals(1, g.edgeCount());
		assertTrue(g.adjacent(42, 43));
		assertEquals(1, g.edges(42, 43).count());
	}

	@Test
	public void testTwoAdjacentVertices() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);
		assertEquals(2, g.vertexCount());
		assertFalse(g.adjacent(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		assertEquals(1, g.edgeCount());
		assertTrue(g.adjacent(42, 43));
	}

	@Test
	public void testAdjacentVertices() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);
		g.addVertex(44);
		assertEquals(3, g.vertexCount());
		assertFalse(g.adjacent(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		assertEquals(1, g.edgeCount());
		assertTrue(g.adjacent(42, 43));
		g.addEdge(new UndirectedEdge(42, 44));
		assertEquals(2, g.edgeCount());
		assertTrue(g.adjacent(42, 44));
		assertEquals(2, g.degree(42));
		assertEquals(1, g.degree(43));
		assertEquals(1, g.degree(44));
	}

	@Test
	public void testDoubleEdge() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);
		assertEquals(2, g.vertexCount());
		assertFalse(g.adjacent(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		assertEquals(1, g.edgeCount());
		assertTrue(g.adjacent(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		assertEquals(2, g.edgeCount());
	}

	@Test
	public void testRemoveEdge() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);

		assertEquals(0, g.degree(42));
		assertEquals(0, g.degree(43));

		g.addEdge(new UndirectedEdge(42, 43));
		assertTrue(g.adjacent(42, 43));
		assertEquals(1, g.degree(42));
		assertEquals(1, g.degree(43));

		g.addEdge(new UndirectedEdge(42, 43));
		assertTrue(g.adjacent(42, 43));
		assertEquals(2, g.degree(42));
		assertEquals(2, g.degree(43));

		g.removeEdge(42, 43);
		assertTrue(g.adjacent(42, 43));
		assertEquals(1, g.degree(42));
		assertEquals(1, g.degree(43));

		g.removeEdge(42, 43);
		assertFalse(g.adjacent(42, 43));
		assertEquals(0, g.degree(42));
		assertEquals(0, g.degree(43));
	}

	@Test
	public void testRemoveEdges() {
		Multigraph g = new DefaultMultigraph();
		g.addVertex(42);
		g.addVertex(43);
		g.addEdge(new UndirectedEdge(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		g.addEdge(new UndirectedEdge(42, 43));
		g.removeEdges();
		assertFalse(g.adjacent(42, 43));
		assertEquals(0, g.degree(42));
		assertEquals(0, g.degree(43));
	}

	@Test
	public void testDualOfGrid() {
		Multigraph g = GraphUtils.dualGraphOfGrid(4, 3);
		assertEquals(7, g.vertexCount());
		assertEquals(10, g.degree(-1));
		assertTrue(g.adjacent(-1, 0));
		assertTrue(g.adjacent(-1, 1));
		assertTrue(g.adjacent(-1, 2));
		assertTrue(g.adjacent(-1, 3));
		assertTrue(g.adjacent(-1, 4));
		assertTrue(g.adjacent(-1, 5));
		assertTrue(g.adjacent(0, 1));
		assertTrue(g.adjacent(1, 2));
		assertTrue(g.adjacent(3, 4));
		assertTrue(g.adjacent(4, 5));
		assertFalse(g.adjacent(0, 2));
		assertFalse(g.adjacent(0, 4));
		assertFalse(g.adjacent(1, 3));
		assertFalse(g.adjacent(1, 5));
	}
}
