package de.amr.easy.maze.tests;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
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
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.misc.StopWatch;

public class MazeGeneratorPerfTest {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 1000;

	private ObservableGrid2D<TraversalState> grid;
	private StopWatch watch;
	private Integer startCell;

	@Before
	public void setUp() {
		grid = new ObservableGrid<>(WIDTH, HEIGHT, UNVISITED);
		startCell = grid.cell(CENTER);
		watch = new StopWatch();
		System.out.println(String.format("Grid size: %dx%d = %d cells", WIDTH, HEIGHT, WIDTH*HEIGHT));
		new IterativeDFS(grid).accept(startCell); // warmup
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGenerationTime() {
		exec(new BinaryTree(grid));
		exec(new BinaryTreeRandom(grid));
		exec(new Eller(grid));
		exec(new EllerInsideOut(grid));
		exec(new GrowingTree(grid));
		exec(new HuntAndKill(grid));
		exec(new HuntAndKillRandom(grid));
		exec(new IterativeDFS(grid));
		exec(new KruskalMST(grid));
		exec(new PrimMST(grid));
		exec(new RandomBFS(grid));
		exec(new RecursiveDivision(grid));
		exec(new Sidewinder(grid));
	}

	private void exec(MazeAlgorithm alg) {
		grid.removeEdges();
		grid.clear();
		watch.start();
		alg.accept(startCell);
		watch.stop();
		System.out.println(alg.getClass().getSimpleName() + ": " + watch.getDuration());
	}
}
