package de.amr.demos.maze.swingapp.model;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.MST;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.SmallGrid;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Traversal;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.UST;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.BFS;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.CHEBYSHEV;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.DFS;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.EUCLIDEAN;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;

import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.impl.traversal.HillClimbing;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKill;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteBFSMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteBestFSMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteDFSMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteHillClimbingMST;
import de.amr.easy.maze.alg.traversal.IterativeDFS;
import de.amr.easy.maze.alg.traversal.RandomBFS;
import de.amr.easy.maze.alg.traversal.RecursiveDFS;
import de.amr.easy.maze.alg.ust.AldousBroderUST;
import de.amr.easy.maze.alg.ust.AldousBroderWilsonUST;
import de.amr.easy.maze.alg.ust.WilsonUSTCollapsingCircle;
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

/**
 * Data model of the maze demo application.
 * 
 * @author Armin Reichert
 */
public class MazeDemoModel {

	public enum Style {
		WALL_PASSAGES, PEARLS
	};

	public static final AlgorithmInfo[] GENERATOR_ALGORITHMS = {
		/*@formatter:off*/
		new AlgorithmInfo(RecursiveDFS.class, "Depth-First-Traversal (recursive, small grids only!)", Traversal, SmallGrid),
		new AlgorithmInfo(IterativeDFS.class, "Depth-First-Traversal (non-recursive)", Traversal),
		new AlgorithmInfo(RandomBFS.class, "Breadth-First-Traversal", Traversal),
		new AlgorithmInfo(KruskalMST.class, "Kruskal MST", MST),
		new AlgorithmInfo(PrimMST.class, "Prim MST", MST),
		new AlgorithmInfo(BoruvkaMST.class, "Boruvka MST", MST),
		new AlgorithmInfo(ReverseDeleteBFSMST.class, "Reverse-Delete MST (BFS, very slow)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteBestFSMST.class, "Reverse-Delete MST (Best-First-Search, very slow)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteDFSMST.class, "Reverse-Delete MST (DFS, very slow)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteHillClimbingMST.class, "Reverse-Delete MST (Hill-Climbing, very slow)", MST, Slow),
		new AlgorithmInfo(AldousBroderUST.class, "Aldous-Broder UST (rather slow)", UST, Slow),
		new AlgorithmInfo(AldousBroderWilsonUST.class, "Houston UST (rather slow)", UST, Slow),
		new AlgorithmInfo(WilsonUSTRandomCell.class, "Wilson UST (random)", UST, Slow),
		new AlgorithmInfo(WilsonUSTRowsTopDown.class, "Wilson UST (row-wise top-to-bottom)", UST),
		new AlgorithmInfo(WilsonUSTLeftToRightSweep.class, "Wilson UST (column-wise left to right)", UST),
		new AlgorithmInfo(WilsonUSTRightToLeftSweep.class, "Wilson UST (column-wise right to left)", UST),
		new AlgorithmInfo(WilsonUSTCollapsingWalls.class, "Wilson UST (column-wise collapsing)", UST),
		new AlgorithmInfo(WilsonUSTCollapsingRectangle.class, "Wilson UST (collapsing rectangle)", UST),
		new AlgorithmInfo(WilsonUSTExpandingCircle.class, "Wilson UST (expanding circle)", UST),
		new AlgorithmInfo(WilsonUSTCollapsingCircle.class, "Wilson UST (collapsing circle)", UST),
		new AlgorithmInfo(WilsonUSTExpandingCircles.class, "Wilson UST (expanding circles)", UST),
		new AlgorithmInfo(WilsonUSTExpandingSpiral.class, "Wilson UST (expanding spiral)", UST),
		new AlgorithmInfo(WilsonUSTExpandingRectangle.class, "Wilson UST (expanding rectangle)", UST),
		new AlgorithmInfo(WilsonUSTNestedRectangles.class, "Wilson UST (nested rectangles)", UST),
		new AlgorithmInfo(WilsonUSTRecursiveCrosses.class, "Wilson UST (recursive crosses)", UST),
		new AlgorithmInfo(WilsonUSTHilbertCurve.class, "Wilson UST (Hilbert curve)", UST),
		new AlgorithmInfo(WilsonUSTMooreCurve.class, "Wilson UST (Moore curve)", UST),
		new AlgorithmInfo(WilsonUSTPeanoCurve.class, "Wilson UST (Peano curve)", UST),
		new AlgorithmInfo(BinaryTree.class, "Binary Tree (row-wise, top-to-bottom)"),
		new AlgorithmInfo(BinaryTreeRandom.class, "Binary Tree (random)"), 
		new AlgorithmInfo(Sidewinder.class, "Sidewinder"),
		new AlgorithmInfo(Eller.class, "Eller's Algorithm"), 
		new AlgorithmInfo(EllerInsideOut.class, "Armin's Algorithm"), 
		new AlgorithmInfo(HuntAndKill.class, "Hunt-And-Kill"),
		new AlgorithmInfo(HuntAndKillRandom.class, "Hunt-And-Kill (random)"),
		new AlgorithmInfo(GrowingTree.class, "Growing Tree"),
		new AlgorithmInfo(RecursiveDivision.class, "Recursive Division"),
		/*@formatter:on*/
	};

	public static final AlgorithmInfo[] PATHFINDER_ALGORITHMS = {
			new AlgorithmInfo(BreadthFirstTraversal.class, "Breadth-First-Search", BFS),
			new AlgorithmInfo(DepthFirstTraversal2.class, "Depth-First-Search", DFS), null,
			new AlgorithmInfo(BestFirstTraversal.class, "Best-First-Search (Manhattan)", BFS, MANHATTAN),
			new AlgorithmInfo(BestFirstTraversal.class, "Best-First-Search (Euclidean)", BFS, EUCLIDEAN),
			new AlgorithmInfo(BestFirstTraversal.class, "Best-First-Search (Chebyshev)", BFS, CHEBYSHEV), null,
			new AlgorithmInfo(HillClimbing.class, "Hill Climbing (Manhattan)", DFS, MANHATTAN),
			new AlgorithmInfo(HillClimbing.class, "Hill Climbing (Euclidean)", DFS, EUCLIDEAN),
			new AlgorithmInfo(HillClimbing.class, "Hill Climbing (Chebyshev)", DFS, CHEBYSHEV),

	};

	public static Optional<AlgorithmInfo> find(AlgorithmInfo[] algorithms, Class<?> clazz) {
		return Arrays.stream(algorithms).filter(alg -> alg.getAlgorithmClass() == clazz).findFirst();
	}

	private MazeGrid grid;
	private int[] gridCellSizes;
	private int gridCellSize;
	private int passageWidthPercentage;
	private boolean generationAnimated;
	private boolean hidingControlsWhenRunning;
	private int delay;
	private GridPosition generationStart;
	private GridPosition pathFinderStart;
	private GridPosition pathFinderTarget;
	private Color unvisitedCellColor;
	private Color visitedCellColor;
	private Color completedCellColor;
	private Color pathColor;
	private Style style;

	public int[] getGridCellSizes() {
		return gridCellSizes;
	}

	public void setGridCellSizes(int... gridCellSizes) {
		this.gridCellSizes = gridCellSizes;
	}

	public int getGridCellSize() {
		return gridCellSize;
	}

	public void setGridCellSize(int gridCellSize) {
		this.gridCellSize = gridCellSize;
	}

	public int getPassageWidthPercentage() {
		return passageWidthPercentage;
	}

	public void setPassageWidthPercentage(int percent) {
		this.passageWidthPercentage = percent;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public boolean isGenerationAnimated() {
		return generationAnimated;
	}

	public void setGenerationAnimated(boolean generationAnimated) {
		this.generationAnimated = generationAnimated;
	}

	public boolean isHidingControlsWhenRunning() {
		return hidingControlsWhenRunning;
	}

	public void setHidingControlsWhenRunning(boolean hidingControlsWhenRunning) {
		this.hidingControlsWhenRunning = hidingControlsWhenRunning;
	}

	public MazeGrid getGrid() {
		return grid;
	}

	public void setGrid(MazeGrid grid) {
		this.grid = grid;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public GridPosition getGenerationStart() {
		return generationStart;
	}

	public void setGenerationStart(GridPosition pos) {
		this.generationStart = pos;
	}

	public GridPosition getPathFinderSource() {
		return pathFinderStart;
	}

	public void setPathFinderStart(GridPosition pos) {
		this.pathFinderStart = pos;
	}

	public GridPosition getPathFinderTarget() {
		return pathFinderTarget;
	}

	public void setPathFinderTarget(GridPosition pos) {
		this.pathFinderTarget = pos;
	}

	public Color getUnvisitedCellColor() {
		return unvisitedCellColor;
	}

	public void setUnvisitedCellColor(Color unvisitedCellColor) {
		this.unvisitedCellColor = unvisitedCellColor;
	}

	public Color getVisitedCellColor() {
		return visitedCellColor;
	}

	public void setVisitedCellColor(Color visitedCellColor) {
		this.visitedCellColor = visitedCellColor;
	}

	public Color getCompletedCellColor() {
		return completedCellColor;
	}

	public void setCompletedCellColor(Color completedCellColor) {
		this.completedCellColor = completedCellColor;
	}

	public Color getPathColor() {
		return pathColor;
	}

	public void setPathColor(Color pathColor) {
		this.pathColor = pathColor;
	}
}
