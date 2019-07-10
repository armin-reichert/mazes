package de.amr.maze.tests;

import static de.amr.graph.core.api.TraversalState.UNVISITED;
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
import de.amr.graph.grid.impl.GridFactory;
import de.amr.graph.grid.impl.Top4;
import de.amr.graph.util.GraphUtils;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.mst.BoruvkaMST;
import de.amr.maze.alg.mst.KruskalMST;
import de.amr.maze.alg.mst.PrimMST;
import de.amr.maze.alg.others.Armin;
import de.amr.maze.alg.others.BinaryTree;
import de.amr.maze.alg.others.BinaryTreeRandom;
import de.amr.maze.alg.others.Eller;
import de.amr.maze.alg.others.HuntAndKill;
import de.amr.maze.alg.others.HuntAndKillRandom;
import de.amr.maze.alg.others.RecursiveDivision;
import de.amr.maze.alg.others.Sidewinder;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysFirst;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysLast;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysRandom;
import de.amr.maze.alg.traversal.GrowingTreeLastOrRandom;
import de.amr.maze.alg.traversal.IterativeDFS;
import de.amr.maze.alg.traversal.RandomBFS;
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

	private static List<String> REPORT;

	private GridGraph2D<TraversalState, Integer> grid;

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
		grid = GridFactory.emptyGrid(WIDTH, HEIGHT, Top4.get(), UNVISITED, 0);
	}

	@After
	public void tearDown() {
		assertEquals(grid.numVertices() - 1, grid.numEdges());
		assertFalse(GraphUtils.containsCycle(grid));
	}

	private void test(MazeGenerator algorithm) {
		StopWatch watch = new StopWatch();
		watch.measure(() -> algorithm.createMaze(0, 0));
		REPORT.add(format("%-30s (%6d cells): %.3f sec", algorithm.getClass().getSimpleName(),
				grid.numVertices(), watch.getSeconds()));
	}

	@Test
	public void testAldousBroder() {
		test(new AldousBroderUST(grid));
	}

	@Test
	public void testAldousBroderWilson() {
		test(new AldousBroderWilsonUST(grid));
	}

	@Test
	public void testBinaryTree() {
		test(new BinaryTree(grid));
	}

	@Test
	public void testBinaryTreeRandom() {
		test(new BinaryTreeRandom(grid));
	}

	@Test
	public void testBoruvka() {
		test(new BoruvkaMST(grid));
	}

	@Test
	public void testEller() {
		test(new Eller(grid));
	}

	@Test
	public void testEllerInsideOut() {
		test(new Armin(grid));
	}

	@Test
	public void testGrowingTreeLastOrRandom() {
		test(new GrowingTreeLastOrRandom(grid));
	}

	@Test
	public void testGrowingTreeAlwaysFirst() {
		test(new GrowingTreeAlwaysFirst(grid));
	}

	@Test
	public void testGrowingTreeAlwaysLast() {
		test(new GrowingTreeAlwaysLast(grid));
	}

	@Test
	public void testGrowingTreeAlwaysRandom() {
		test(new GrowingTreeAlwaysRandom(grid));
	}

	@Test
	public void testHuntAndKill() {
		test(new HuntAndKill(grid));
	}

	@Test
	public void testHuntAndKillRandom() {
		test(new HuntAndKillRandom(grid));
	}

	@Test
	public void testIterativeDFS() {
		test(new IterativeDFS(grid));
	}

	@Test
	public void testKruskal() {
		test(new KruskalMST(grid));
	}

	@Test
	public void testPrim() {
		test(new PrimMST(grid));
	}

	@Test
	public void testRandomBFS() {
		test(new RandomBFS(grid));
	}

	@Test
	public void testRecursiveDivision() {
		test(new RecursiveDivision(grid));
	}

	@Test
	public void testSideWinder() {
		test(new Sidewinder(grid));
	}

	// TODO why does this test fail?
	// @Test
	// public void testWilsonUSTCollapsingCircle() {
	// test(new WilsonUSTCollapsingCircle());
	// }

	@Test
	public void testWilsonUSTCollapsingRectangle() {
		test(new WilsonUSTCollapsingRectangle(grid));
	}

	@Test
	public void testWilsonUSTCollapsingWalls() {
		test(new WilsonUSTCollapsingWalls(grid));
	}

	@Test
	public void testWilsonUSTExpandingCircle() {
		test(new WilsonUSTExpandingCircle(grid));
	}

	@Test
	public void testWilsonUSTExpandingCircles() {
		test(new WilsonUSTExpandingCircles(grid));
	}

	@Test
	public void testWilsonUSTExpandingRectangle() {
		test(new WilsonUSTExpandingRectangle(grid));
	}

	@Test
	public void testWilsonUSTExpandingSpiral() {
		test(new WilsonUSTExpandingSpiral(grid));
	}

	@Test
	public void testWilsonUSTHilbertCurve() {
		test(new WilsonUSTHilbertCurve(grid));
	}

	@Test
	public void testWilsonUSTLeftToRightSweep() {
		test(new WilsonUSTLeftToRightSweep(grid));
	}

	@Test
	public void testWilsonUSTMooreCurve() {
		test(new WilsonUSTMooreCurve(grid));
	}

	@Test
	public void testWilsonUSTNestedRectangles() {
		test(new WilsonUSTNestedRectangles(grid));
	}

	@Test
	public void testWilsonUSTPeanoCurve() {
		test(new WilsonUSTPeanoCurve(grid));
	}

	@Test
	public void testWilsonUSTRandomCell() {
		test(new WilsonUSTRandomCell(grid));
	}

	@Test
	public void testWilsonUSTRecursiveCrosses() {
		test(new WilsonUSTRecursiveCrosses(grid));
	}

	@Test
	public void testWilsonUSTRightToLeftSweep() {
		test(new WilsonUSTRightToLeftSweep(grid));
	}

	@Test
	public void testWilsonUSTRowsTopDown() {
		test(new WilsonUSTRowsTopDown(grid));
	}
}