package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.impl.ObservableDataGrid;
import de.amr.easy.maze.algorithms.AldousBroderUST;
import de.amr.easy.maze.algorithms.BinaryTree;
import de.amr.easy.maze.algorithms.BinaryTreeRandom;
import de.amr.easy.maze.algorithms.Eller;
import de.amr.easy.maze.algorithms.EllerInsideOut;
import de.amr.easy.maze.algorithms.HuntAndKill;
import de.amr.easy.maze.algorithms.HuntAndKillRandom;
import de.amr.easy.maze.algorithms.IterativeDFS;
import de.amr.easy.maze.algorithms.KruskalMST;
import de.amr.easy.maze.algorithms.PrimMST;
import de.amr.easy.maze.algorithms.RandomBFS;
import de.amr.easy.maze.algorithms.RecursiveDFS;
import de.amr.easy.maze.algorithms.RecursiveDivision;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTCollapsingCircle;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTCollapsingRectangle;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTCollapsingWalls;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTExpandingCircle;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTExpandingCircles;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTExpandingRectangle;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTExpandingSpiral;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTHilbertCurve;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTLeftToRightSweep;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTNestedRectangles;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTPeanoCurve;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRandomCell;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRowsTopDown;

public class MazeGeneratorTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;

	private ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;

	@Before
	public void setUp() {
		grid = new ObservableDataGrid<>(WIDTH, HEIGHT, UNVISITED);
		assertEquals(grid.vertexCount(), WIDTH * HEIGHT);
		assertEquals(grid.edgeCount(), 0);
	}

	@After
	public void tearDown() {
		assertEquals(grid.edgeCount(), grid.vertexCount() - 1);
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
		grid = new ObservableDataGrid<>(50, 40, UNVISITED);
		new RecursiveDFS(grid).accept(grid.cell(CENTER));
	}

	@Test
	public void testRecursiveDivision() {
		new RecursiveDivision(grid).accept(grid.cell(CENTER));
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
