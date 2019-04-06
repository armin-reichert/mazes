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
import de.amr.maze.alg.Armin;
import de.amr.maze.alg.BinaryTree;
import de.amr.maze.alg.BinaryTreeRandom;
import de.amr.maze.alg.Eller;
import de.amr.maze.alg.HuntAndKill;
import de.amr.maze.alg.HuntAndKillRandom;
import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.Sidewinder;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.core.UnobservableMazesFactory;
import de.amr.maze.alg.mst.BoruvkaMST;
import de.amr.maze.alg.mst.KruskalMST;
import de.amr.maze.alg.mst.PrimMST;
import de.amr.maze.alg.mst.ReverseDeleteMST_BFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_BestFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_DFS;
import de.amr.maze.alg.mst.ReverseDeleteMST_HillClimbing;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysFirst;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysLast;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysRandom;
import de.amr.maze.alg.traversal.GrowingTreeLastOrRandom;
import de.amr.maze.alg.traversal.IterativeDFS;
import de.amr.maze.alg.traversal.RandomBFS;
import de.amr.maze.alg.traversal.RecursiveDFS;
import de.amr.maze.alg.ust.AldousBroderUST;
import de.amr.maze.alg.ust.AldousBroderWilsonUST;
import de.amr.maze.alg.ust.WilsonUSTCollapsingRectangle;
import de.amr.maze.alg.ust.WilsonUSTCollapsingWalls;
import de.amr.maze.alg.ust.WilsonUSTExpandingCircle;
import de.amr.maze.alg.ust.WilsonUSTExpandingCircles;
import de.amr.maze.alg.ust.WilsonUSTExpandingRectangle;
import de.amr.maze.alg.ust.WilsonUSTExpandingSpiral;
import de.amr.maze.alg.ust.WilsonUSTHilbertCurve;
import de.amr.maze.alg.ust.WilsonUSTLeftToRightSweep;
import de.amr.maze.alg.ust.WilsonUSTMooreCurve;
import de.amr.maze.alg.ust.WilsonUSTNestedRectangles;
import de.amr.maze.alg.ust.WilsonUSTPeanoCurve;
import de.amr.maze.alg.ust.WilsonUSTRandomCell;
import de.amr.maze.alg.ust.WilsonUSTRecursiveCrosses;
import de.amr.maze.alg.ust.WilsonUSTRightToLeftSweep;
import de.amr.maze.alg.ust.WilsonUSTRowsTopDown;
import de.amr.util.StopWatch;

public class MazeGeneratorTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;
	private static final int WIDTH_SMALL = 25;
	private static final int HEIGHT_SMALL = 25;

	private static List<String> REPORT;

	private GridGraph2D<TraversalState, Integer> grid;

	@BeforeClass
	public static void beforeAllTests() {
		// warm-up (class loading etc.)
		new RandomBFS(UnobservableMazesFactory.get(), WIDTH, HEIGHT).createMaze(0, 0);
		REPORT = new ArrayList<>();
	}

	@AfterClass
	public static void afterAllTests() {
		Collections.sort(REPORT);
		REPORT.forEach(out::println);
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		assertEquals(grid.numVertices() - 1, grid.numEdges());
		assertFalse(GraphUtils.containsCycle(grid));
	}

	private void test(MazeGenerator algorithm) {
		StopWatch watch = new StopWatch();
		watch.measure(() -> grid = algorithm.createMaze(0, 0));
		REPORT.add(format("%-30s (%6d cells): %.3f sec", algorithm.getClass().getSimpleName(), grid.numVertices(),
				watch.getSeconds()));

	}

	@Test
	public void testAldousBroder() {
		test(new AldousBroderUST(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testAldousBroderWilson() {
		test(new AldousBroderWilsonUST(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testBinaryTree() {
		test(new BinaryTree(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testBinaryTreeRandom() {
		test(new BinaryTreeRandom(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testBoruvka() {
		test(new BoruvkaMST(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testEller() {
		test(new Eller(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testEllerInsideOut() {
		test(new Armin(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testGrowingTreeLastOrRandom() {
		test(new GrowingTreeLastOrRandom(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testGrowingTreeAlwaysFirst() {
		test(new GrowingTreeAlwaysFirst(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testGrowingTreeAlwaysLast() {
		test(new GrowingTreeAlwaysLast(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testGrowingTreeAlwaysRandom() {
		test(new GrowingTreeAlwaysRandom(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testHuntAndKill() {
		test(new HuntAndKill(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testHuntAndKillRandom() {
		test(new HuntAndKillRandom(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testIterativeDFS() {
		test(new IterativeDFS(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testKruskal() {
		test(new KruskalMST(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testPrim() {
		test(new PrimMST(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testRandomBFS() {
		test(new RandomBFS(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testRecursiveDFS() {
		test(new RecursiveDFS(UnobservableMazesFactory.get(), WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testRecursiveDivision() {
		test(new RecursiveDivision(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testReverseDeleteDFSMST() {
		test(new ReverseDeleteMST_DFS(UnobservableMazesFactory.get(), WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteBestFSMST() {
		test(new ReverseDeleteMST_BestFS(UnobservableMazesFactory.get(), WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteBFSMST() {
		test(new ReverseDeleteMST_BFS(UnobservableMazesFactory.get(), WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteHillClimbingMST() {
		test(new ReverseDeleteMST_HillClimbing(UnobservableMazesFactory.get(), WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testSideWinder() {
		test(new Sidewinder(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	// TODO why does this test fail?
	// @Test
	// public void testWilsonUSTCollapsingCircle() {
	// test(new WilsonUSTCollapsingCircle());
	// }

	@Test
	public void testWilsonUSTCollapsingRectangle() {
		test(new WilsonUSTCollapsingRectangle(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTCollapsingWalls() {
		test(new WilsonUSTCollapsingWalls(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingCircle() {
		test(new WilsonUSTExpandingCircle(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingCircles() {
		test(new WilsonUSTExpandingCircles(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingRectangle() {
		test(new WilsonUSTExpandingRectangle(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingSpiral() {
		test(new WilsonUSTExpandingSpiral(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTHilbertCurve() {
		test(new WilsonUSTHilbertCurve(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTLeftToRightSweep() {
		test(new WilsonUSTLeftToRightSweep(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTMooreCurve() {
		test(new WilsonUSTMooreCurve(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTNestedRectangles() {
		test(new WilsonUSTNestedRectangles(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTPeanoCurve() {
		test(new WilsonUSTPeanoCurve(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRandomCell() {
		test(new WilsonUSTRandomCell(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRecursiveCrosses() {
		test(new WilsonUSTRecursiveCrosses(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRightToLeftSweep() {
		test(new WilsonUSTRightToLeftSweep(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRowsTopDown() {
		test(new WilsonUSTRowsTopDown(UnobservableMazesFactory.get(), WIDTH, HEIGHT));
	}
}