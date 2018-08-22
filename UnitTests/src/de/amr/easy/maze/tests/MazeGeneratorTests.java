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

import de.amr.easy.grid.impl.OrthogonalGrid;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKill;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST_BFS;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST_BestFS;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST_DFS;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST_HillClimbing;
import de.amr.easy.maze.alg.traversal.IterativeDFS;
import de.amr.easy.maze.alg.traversal.RandomBFS;
import de.amr.easy.maze.alg.traversal.RecursiveDFS;
import de.amr.easy.maze.alg.ust.AldousBroderUST;
import de.amr.easy.maze.alg.ust.AldousBroderWilsonUST;
import de.amr.easy.maze.alg.ust.WilsonUSTCollapsingRectangle;
import de.amr.easy.maze.alg.ust.WilsonUSTCollapsingWalls;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircle;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircles;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingRectangle;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingSpiral;
import de.amr.easy.maze.alg.ust.WilsonUSTHilbertCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTLeftToRightSweep;
import de.amr.easy.maze.alg.ust.WilsonUSTMooreCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTNestedRectangles;
import de.amr.easy.maze.alg.ust.WilsonUSTPeanoCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTRandomCell;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.alg.ust.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.alg.ust.WilsonUSTRowsTopDown;
import de.amr.easy.util.GraphUtils;
import de.amr.easy.util.StopWatch;

public class MazeGeneratorTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;
	private static final int WIDTH_SMALL = 25;
	private static final int HEIGHT_SMALL = 25;

	private static List<String> REPORT;

	private OrthogonalGrid grid;

	@BeforeClass
	public static void beforeAllTests() {
		// warm-up (class loading etc.)
		new RandomBFS(WIDTH, HEIGHT).createMaze(0, 0);
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
		assertEquals(grid.numEdges(), grid.numVertices() - 1);
		assertFalse(GraphUtils.containsCycle(grid));
	}

	private void test(MazeGenerator<OrthogonalGrid> algorithm) {
		StopWatch watch = new StopWatch();
		watch.measure(() -> grid = algorithm.createMaze(0, 0));
		REPORT.add(format("%-30s (%6d cells): %.3f sec", algorithm.getClass().getSimpleName(), grid.numVertices(),
				watch.getSeconds()));

	}

	@Test
	public void testAldousBroder() {
		test(new AldousBroderUST(WIDTH, HEIGHT));
	}

	@Test
	public void testAldousBroderWilson() {
		test(new AldousBroderWilsonUST(WIDTH, HEIGHT));
	}

	@Test
	public void testBinaryTree() {
		test(new BinaryTree(WIDTH, HEIGHT));
	}

	@Test
	public void testBinaryTreeRandom() {
		test(new BinaryTreeRandom(WIDTH, HEIGHT));
	}

	@Test
	public void testBoruvka() {
		test(new BoruvkaMST(WIDTH, HEIGHT));
	}

	@Test
	public void testEller() {
		test(new Eller(WIDTH, HEIGHT));
	}

	@Test
	public void testEllerInsideOut() {
		test(new EllerInsideOut(WIDTH, HEIGHT));
	}

	@Test
	public void testGrowingTree() {
		test(new GrowingTree(WIDTH, HEIGHT));
	}

	@Test
	public void testHuntAndKill() {
		test(new HuntAndKill(WIDTH, HEIGHT));
	}

	@Test
	public void testHuntAndKillRandom() {
		test(new HuntAndKillRandom(WIDTH, HEIGHT));
	}

	@Test
	public void testIterativeDFS() {
		test(new IterativeDFS(WIDTH, HEIGHT));
	}

	@Test
	public void testKruskal() {
		test(new KruskalMST(WIDTH, HEIGHT));
	}

	// @Test
	public void testPrim() {
		test(new PrimMST(WIDTH, HEIGHT));
	}

	@Test
	public void testRandomBFS() {
		test(new RandomBFS(WIDTH, HEIGHT));
	}

	@Test
	public void testRecursiveDFS() {
		test(new RecursiveDFS(WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testRecursiveDivision() {
		test(new RecursiveDivision(WIDTH, HEIGHT));
	}

	@Test
	public void testReverseDeleteDFSMST() {
		test(new ReverseDeleteMST_DFS(WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteBestFSMST() {
		test(new ReverseDeleteMST_BestFS(WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteBFSMST() {
		test(new ReverseDeleteMST_BFS(WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testReverseDeleteHillClimbingMST() {
		test(new ReverseDeleteMST_HillClimbing(WIDTH_SMALL, HEIGHT_SMALL));
	}

	@Test
	public void testSideWinder() {
		test(new Sidewinder(WIDTH, HEIGHT));
	}

	// TODO why does this test fail?
	// @Test
	// public void testWilsonUSTCollapsingCircle() {
	// test(new WilsonUSTCollapsingCircle());
	// }

	@Test
	public void testWilsonUSTCollapsingRectangle() {
		test(new WilsonUSTCollapsingRectangle(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTCollapsingWalls() {
		test(new WilsonUSTCollapsingWalls(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingCircle() {
		test(new WilsonUSTExpandingCircle(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingCircles() {
		test(new WilsonUSTExpandingCircles(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingRectangle() {
		test(new WilsonUSTExpandingRectangle(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTExpandingSpiral() {
		test(new WilsonUSTExpandingSpiral(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTHilbertCurve() {
		test(new WilsonUSTHilbertCurve(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTLeftToRightSweep() {
		test(new WilsonUSTLeftToRightSweep(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTMooreCurve() {
		test(new WilsonUSTMooreCurve(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTNestedRectangles() {
		test(new WilsonUSTNestedRectangles(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTPeanoCurve() {
		test(new WilsonUSTPeanoCurve(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRandomCell() {
		test(new WilsonUSTRandomCell(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRecursiveCrosses() {
		test(new WilsonUSTRecursiveCrosses(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRightToLeftSweep() {
		test(new WilsonUSTRightToLeftSweep(WIDTH, HEIGHT));
	}

	@Test
	public void testWilsonUSTRowsTopDown() {
		test(new WilsonUSTRowsTopDown(WIDTH, HEIGHT));
	}
}