package de.amr.easy.grid.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import de.amr.graph.grid.impl.GridGraph;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.util.GraphUtils;
import de.amr.maze.alg.traversal.RandomBFS;

/**
 * Test case for {@link GridGraph}
 * 
 * @author Armin Reichert
 */
public class GridTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	@After
	public void tearDown() {
	}

	@Test
	public void testCycleCheckerSpanningTree() {
		// create a spanning tree
		OrthogonalGrid grid = new RandomBFS(WIDTH, HEIGHT).createMaze(0, 0);
		assertFalse(GraphUtils.containsCycle(grid));

		// Find vertex with non-adjacent neighbor. Adding an edge to this neighbor produces a cycle.
		/*@formatter:off*/
		grid.vertices()
			.filter(cell -> grid.neighbors(cell).anyMatch(neighbor -> !grid.adjacent(cell, neighbor)))
			.findAny()
			.ifPresent(cell -> 	
				grid.neighbors(cell)
					.filter(neighbor -> !grid.adjacent(cell, neighbor))
					.findAny()
					.ifPresent(neighbor -> grid.addEdge(cell, neighbor)));
		/*@formatter:on*/

		// now there must be a cycle
		assertTrue(GraphUtils.containsCycle(grid));
	}

}