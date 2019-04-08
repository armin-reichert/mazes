package de.amr.easy.maze.tests;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.util.GraphUtils;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.UnobservableGridFactory;
import de.amr.maze.alg.mst.ReverseDeleteMST_BFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_BestFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_DFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_HillClimbing;
import de.amr.maze.alg.traversal.RecursiveDFS;
import de.amr.util.StopWatch;

public class MazeGeneratorTestSmall {

	private static final int WIDTH_SMALL = 25;
	private static final int HEIGHT_SMALL = 25;

	private static List<String> REPORT;

	private GridGraph2D<TraversalState, Integer> smallGrid;

	@BeforeClass
	public static void beforeAllTests() {
		REPORT = new ArrayList<>();
	}

	@AfterClass
	public static void afterAllTests() {
		Collections.sort(REPORT);
		REPORT.forEach(out::println);
	}

	@Before
	public void setUp() {
		smallGrid = UnobservableGridFactory.get().emptyGrid(WIDTH_SMALL, HEIGHT_SMALL, TraversalState.UNVISITED);
	}

	@After
	public void tearDown() {
		assertEquals(smallGrid.numVertices() - 1, smallGrid.numEdges());
		assertFalse(GraphUtils.containsCycle(smallGrid));
	}

	private void test(MazeGenerator algorithm) {
		StopWatch watch = new StopWatch();
		watch.measure(() -> algorithm.createMaze(0, 0));
		REPORT.add(format("%-30s (%6d cells): %.3f sec", algorithm.getClass().getSimpleName(),
				smallGrid.numVertices(), watch.getSeconds()));
	}

	@Test
	public void testRecursiveDFS() {
		test(new RecursiveDFS(smallGrid));
	}

	@Test
	public void testReverseDeleteDFSMST() {
		test(new ReverseDeleteMST_DFS(smallGrid));
	}

	@Test
	public void testReverseDeleteBestFSMST() {
		test(new ReverseDeleteMST_BestFS(smallGrid));
	}

	@Test
	public void testReverseDeleteBFSMST() {
		test(new ReverseDeleteMST_BFS(smallGrid));
	}

	@Test
	public void testReverseDeleteHillClimbingMST() {
		test(new ReverseDeleteMST_HillClimbing(smallGrid));
	}
}