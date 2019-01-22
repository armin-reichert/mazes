package de.amr.easy.graph.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.core.api.Edge;
import de.amr.easy.graph.core.api.UndirectedEdge;
import de.amr.easy.graph.core.impl.UGraph;

public class UGraphTest {

	private UGraph<Void, Void> g;

	@Before
	public void setUp() {
		g = new UGraph<>();
	}

	@Test
	public void testCreation() {
		assertEquals(0, g.numVertices());
		assertEquals(0, g.numEdges());
	}

	@Test
	public void testAddVertex() {
		g.addVertex(42);
		assertEquals(1, g.numVertices());
		assertTrue(g.vertices().count() == 1);
		assertTrue(g.vertices().filter(v -> v == 42).findAny().isPresent());
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveVertexFromEmptyGraph() {
		g.removeVertex(42);
	}

	@Test
	public void removeVertex() {
		g.addVertex(42);
		g.removeVertex(42);
		assertEquals(0, g.numVertices());
	}

	@Test(expected = IllegalStateException.class)
	public void removeNonexistingVertex() {
		g.removeVertex(42);
	}

	@Test
	public void testUndirectedEdge() {
		UndirectedEdge e1 = new UndirectedEdge(0, 1);
		UndirectedEdge e2 = new UndirectedEdge(1, 0);
		assertEquals(e1, e2);

		g.addVertex(0);
		g.addVertex(1);
		g.addEdge(0, 1);
		assertTrue(g.edge(0, 1).isPresent());
		Edge e = g.edge(0, 1).get();
		assertEquals(e1, e);
		assertEquals(e2, e);

		Map<Edge, Integer> edgeMap = new HashMap<>();
		edgeMap.put(e1, 42);
		assertTrue(edgeMap.get(e) == 42);
		assertTrue(edgeMap.get(e1) == 42);
		assertTrue(edgeMap.get(e2) == 42);
	}

}
