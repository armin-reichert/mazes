package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.alg.CycleFinderUnionFind;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
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

public class MazeGeneratorTests {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private ObservableGrid2D<TraversalState> grid;

	@Before
	public void setUp() {
		grid = new ObservableGrid<>(WIDTH, HEIGHT, UNVISITED);
	}

	@After
	public void tearDown() {
		assertEquals(grid.edgeCount(), grid.vertexCount() - 1);
		assertFalse(new CycleFinderUnionFind<>(grid, grid.cell(CENTER)).isCycleFound());
	}

	@Test
	public void testAldousBroder() {
		new AldousBroderUST(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testBinaryTree() {
		new BinaryTree(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testBinaryTreeRandom() {
		new BinaryTreeRandom(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testEller() {
		new Eller(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testEllerInsideOut() {
		new EllerInsideOut(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testGrowingTree() {
		new GrowingTree(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testHuntAndKill() {
		new HuntAndKill(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testHuntAndKillRandom() {
		new HuntAndKillRandom(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testIterativeDFS() {
		new IterativeDFS(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testKruskal() {
		new KruskalMST(grid).accept(null);
	}

	@Test
	public void testPrim() {
		new PrimMST(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testRandomBFS() {
		new RandomBFS(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testRecursiveDFS() {
		grid = new ObservableGrid<>(50, 40, UNVISITED);
		new RecursiveDFS(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testRecursiveDivision() {
		new RecursiveDivision(grid).accept(grid.cell(CENTER));
	}
	
	@Test
	public void testReverseDeleteMST() {
		grid = new ObservableGrid<>(30, 30, COMPLETED);
		new ReverseDeleteMST(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testSideWinder() {
		new Sidewinder(grid).accept(grid.cell(TOP_LEFT));
	}

	@Test
	public void testWilsonUSTCollapsingCircle() {
		new WilsonUSTCollapsingCircle(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTCollapsingRectangle() {
		new WilsonUSTCollapsingRectangle(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTCollapsingWalls() {
		new WilsonUSTCollapsingWalls(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTExpandingCircle() {
		new WilsonUSTExpandingCircle(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTExpandingCircles() {
		new WilsonUSTExpandingCircles(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTExpandingRectangle() {
		new WilsonUSTExpandingRectangle(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTExpandingSpiral() {
		new WilsonUSTExpandingSpiral(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTHilbertCurve() {
		new WilsonUSTHilbertCurve(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTLeftToRightSweep() {
		new WilsonUSTLeftToRightSweep(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTNestedRectangles() {
		new WilsonUSTNestedRectangles(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTPeanoCurve() {
		new WilsonUSTPeanoCurve(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTRandomCell() {
		new WilsonUSTRandomCell(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTRecursiveCrosses() {
		new WilsonUSTRecursiveCrosses(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTRightToLeftSweep() {
		new WilsonUSTRightToLeftSweep(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testWilsonUSTRowsTopDown() {
		new WilsonUSTRowsTopDown(grid).accept(grid.cell(CENTER));
	}
}
