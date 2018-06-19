package de.amr.easy.graph.tests;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.impl.DefaultGraph;

public class LabeledGraphTests {

	private DefaultGraph<String, SimpleEdge> g;

	@Before
	public void setUp() {
		g = new DefaultGraph<>(SimpleEdge::new);
	}

	@Test(expected = IllegalStateException.class)
	public void testSetLabelOnNonexistingVertex() {
		g.set(0, "A");
	}

	@Test
	public void testSetLabel() {
		g.addVertex(0);
		g.set(0, "A");
		Assert.assertEquals("A", g.get(0));
	}

	@Test
	public void testDefaultVertexLabel() {
		g.setDefaultVertex("42");
		g.addVertex(0);
		Assert.assertEquals("42", g.get(0));
		g.set(0, "43");
		Assert.assertNotEquals("42", g.get(0));
	}

	@Test
	public void testSampleGraph() {
		IntStream.range(0, 8).forEach(g::addVertex);
		g.set(0, "S");
		g.set(1, "A");
		g.set(2, "B");
		g.set(3, "C");
		g.set(4, "D");
		g.set(5, "E");
		g.set(6, "F");
		g.set(7, "G");
		g.addEdge(0, 1); // S-A
		g.addEdge(1, 2); // A-B
		g.addEdge(2, 3); // B-C
		g.addEdge(0, 4); // A-D
		g.addEdge(4, 1); // D-A
		g.addEdge(4, 5); // D-E
		g.addEdge(5, 2); // E-B
		g.addEdge(5, 6); // E-F
		g.addEdge(6, 7); // F-G

		Assert.assertTrue(g.hasEdge(0, 1));
		Assert.assertTrue(g.hasEdge(1, 2));
		Assert.assertTrue(g.hasEdge(2, 3));
		Assert.assertTrue(g.hasEdge(0, 4));
		Assert.assertTrue(g.hasEdge(4, 1));
		Assert.assertTrue(g.hasEdge(4, 5));
		Assert.assertTrue(g.hasEdge(5, 2));
		Assert.assertTrue(g.hasEdge(5, 6));
		Assert.assertTrue(g.hasEdge(6, 7));

	}
}
