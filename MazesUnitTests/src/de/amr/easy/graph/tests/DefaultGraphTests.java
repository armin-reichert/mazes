package de.amr.easy.graph.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.impl.DefaultGraph;

public class DefaultGraphTests {

	private DefaultGraph<Void, Void> g;

	@Before
	public void setUp() {
		g = new DefaultGraph<>(SimpleEdge::new);
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

}
