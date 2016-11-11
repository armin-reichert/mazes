package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
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

import de.amr.easy.graph.alg.CycleChecker;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.dir.Dir4;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.maze.alg.AldousBroderUST;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKill;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.IterativeDFS;
import de.amr.easy.maze.alg.KruskalMST;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.maze.alg.PrimMST;
import de.amr.easy.maze.alg.RandomBFS;
import de.amr.easy.maze.alg.RecursiveDFS;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.ReverseDeleteMST;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.wilson.WilsonUSTCollapsingCircle;
import de.amr.easy.maze.alg.wilson.WilsonUSTCollapsingRectangle;
import de.amr.easy.maze.alg.wilson.WilsonUSTCollapsingWalls;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircle;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircles;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingRectangle;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingSpiral;
import de.amr.easy.maze.alg.wilson.WilsonUSTHilbertCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTLeftToRightSweep;
import de.amr.easy.maze.alg.wilson.WilsonUSTNestedRectangles;
import de.amr.easy.maze.alg.wilson.WilsonUSTPeanoCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTRandomCell;
import de.amr.easy.maze.alg.wilson.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.alg.wilson.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.alg.wilson.WilsonUSTRowsTopDown;
import de.amr.easy.util.StopWatch;

public class MazeGeneratorTests {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 100;

	private Grid2D<Dir4,TraversalState, Integer> grid;
	private StopWatch watch;

	private static List<String> results = new ArrayList<>();

	@BeforeClass
	public static void beforeAllTests() {
		// warm-up
		new RandomBFS(new Grid<>(WIDTH, HEIGHT, UNVISITED)).accept(0);
	}

	@AfterClass
	public static void afterAllTests() {
		Collections.sort(results);
		results.forEach(out::println);
	}

	@Before
	public void setUp() {
		grid = new Grid<>(WIDTH, HEIGHT, UNVISITED);
		watch = new StopWatch();
	}

	@After
	public void tearDown() {
		assertEquals(grid.edgeCount(), grid.vertexCount() - 1);
		assertFalse(new CycleChecker<Integer, WeightedEdge<Integer, Integer>>().test(grid));
	}

	private void exec(MazeAlgorithm algorithm) {
		watch.runAndMeasure(() -> algorithm.accept(grid.cell(CENTER)));
		results.add(format("%-30s (%6d cells): %.3f sec", algorithm.getClass().getSimpleName(), grid.numCells(),
				watch.getSeconds()));
	}

	@Test
	public void testAldousBroder() {
		exec(new AldousBroderUST(grid));
	}

	@Test
	public void testBinaryTree() {
		exec(new BinaryTree(grid));
	}

	@Test
	public void testBinaryTreeRandom() {
		exec(new BinaryTreeRandom(grid));
	}

	@Test
	public void testEller() {
		exec(new Eller(grid));
	}

	@Test
	public void testEllerInsideOut() {
		exec(new EllerInsideOut(grid));
	}

	@Test
	public void testGrowingTree() {
		exec(new GrowingTree(grid));
	}

	@Test
	public void testHuntAndKill() {
		exec(new HuntAndKill(grid));
	}

	@Test
	public void testHuntAndKillRandom() {
		exec(new HuntAndKillRandom(grid));
	}

	@Test
	public void testIterativeDFS() {
		exec(new IterativeDFS(grid));
	}

	@Test
	public void testKruskal() {
		exec(new KruskalMST(grid));
	}

	@Test
	public void testPrim() {
		exec(new PrimMST(grid));
	}

	@Test
	public void testRandomBFS() {
		exec(new RandomBFS(grid));
	}

	@Test
	public void testRecursiveDFS() {
		grid = new Grid<>(40, 50, UNVISITED);
		exec(new RecursiveDFS(grid));
	}

	@Test
	public void testRecursiveDivision() {
		exec(new RecursiveDivision(grid));
	}

	@Test
	public void testReverseDeleteMST() {
		grid = new Grid<>(40, 25, UNVISITED);
		exec(new ReverseDeleteMST(grid));
	}

	@Test
	public void testSideWinder() {
		exec(new Sidewinder(grid));
	}

	@Test
	public void testWilsonUSTCollapsingCircle() {
		exec(new WilsonUSTCollapsingCircle(grid));
	}

	@Test
	public void testWilsonUSTCollapsingRectangle() {
		exec(new WilsonUSTCollapsingRectangle(grid));
	}

	@Test
	public void testWilsonUSTCollapsingWalls() {
		exec(new WilsonUSTCollapsingWalls(grid));
	}

	@Test
	public void testWilsonUSTExpandingCircle() {
		exec(new WilsonUSTExpandingCircle(grid));
	}

	@Test
	public void testWilsonUSTExpandingCircles() {
		exec(new WilsonUSTExpandingCircles(grid));
	}

	@Test
	public void testWilsonUSTExpandingRectangle() {
		exec(new WilsonUSTExpandingRectangle(grid));
	}

	@Test
	public void testWilsonUSTExpandingSpiral() {
		exec(new WilsonUSTExpandingSpiral(grid));
	}

	@Test
	public void testWilsonUSTHilbertCurve() {
		exec(new WilsonUSTHilbertCurve(grid));
	}

	@Test
	public void testWilsonUSTLeftToRightSweep() {
		exec(new WilsonUSTLeftToRightSweep(grid));
	}

	@Test
	public void testWilsonUSTNestedRectangles() {
		exec(new WilsonUSTNestedRectangles(grid));
	}

	@Test
	public void testWilsonUSTPeanoCurve() {
		exec(new WilsonUSTPeanoCurve(grid));
	}

	@Test
	public void testWilsonUSTRandomCell() {
		exec(new WilsonUSTRandomCell(grid));
	}

	@Test
	public void testWilsonUSTRecursiveCrosses() {
		exec(new WilsonUSTRecursiveCrosses(grid));
	}

	@Test
	public void testWilsonUSTRightToLeftSweep() {
		exec(new WilsonUSTRightToLeftSweep(grid));
	}

	@Test
	public void testWilsonUSTRowsTopDown() {
		exec(new WilsonUSTRowsTopDown(grid));
	}
}