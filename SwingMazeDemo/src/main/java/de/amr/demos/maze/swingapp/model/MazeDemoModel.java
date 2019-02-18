package de.amr.demos.maze.swingapp.model;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.MST;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.SmallGrid;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Traversal;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.UST;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.BFS;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.DFS;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.INFORMED;

import java.awt.Color;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import de.amr.graph.grid.api.GridPosition;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.pathfinder.impl.AStarSearch;
import de.amr.graph.pathfinder.impl.BestFirstSearch;
import de.amr.graph.pathfinder.impl.BreadthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch2;
import de.amr.graph.pathfinder.impl.DijkstraSearch;
import de.amr.graph.pathfinder.impl.HillClimbingSearch;
import de.amr.graph.pathfinder.impl.IDDFS;
import de.amr.maze.alg.Armin;
import de.amr.maze.alg.BinaryTree;
import de.amr.maze.alg.BinaryTreeRandom;
import de.amr.maze.alg.Eller;
import de.amr.maze.alg.HuntAndKill;
import de.amr.maze.alg.HuntAndKillRandom;
import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.Sidewinder;
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
import de.amr.maze.alg.ust.WilsonUSTCollapsingCircle;
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

/**
 * Data model of the maze demo application.
 * 
 * @author Armin Reichert
 */
public class MazeDemoModel {

	public enum Style {
		WALL_PASSAGES, PEARLS
	};

	public enum Metric {
		EUCLIDEAN, MANHATTAN, CHEBYSHEV
	}

	public static final AlgorithmInfo[] GENERATOR_ALGORITHMS = {
		/*@formatter:off*/
		new AlgorithmInfo(RecursiveDFS.class, "Random recursive DFS (small grids only!)", Traversal, SmallGrid),
		new AlgorithmInfo(IterativeDFS.class, "Random nonrecursive DFS", Traversal),
		new AlgorithmInfo(RandomBFS.class, "Random BFS", Traversal),
		new AlgorithmInfo(GrowingTreeAlwaysFirst.class, "Growing Tree (always select first)", Traversal),
		new AlgorithmInfo(GrowingTreeAlwaysLast.class, "Growing Tree (always select last)", Traversal),
		new AlgorithmInfo(GrowingTreeAlwaysRandom.class, "Growing Tree (always select random)", Traversal),
		new AlgorithmInfo(GrowingTreeLastOrRandom.class, "Growing Tree (last or random)", Traversal),
		new AlgorithmInfo(KruskalMST.class, "Kruskal MST", MST),
		new AlgorithmInfo(PrimMST.class, "Prim MST", MST),
		new AlgorithmInfo(BoruvkaMST.class, "Boruvka MST", MST),
		new AlgorithmInfo(ReverseDeleteMST_BFS.class, "Reverse-Delete MST (BFS, very slow!)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteMST_BestFS.class, "Reverse-Delete MST (Best-First Search, very slow!)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteMST_DFS.class, "Reverse-Delete MST (DFS, very slow!)", MST, Slow),
		new AlgorithmInfo(ReverseDeleteMST_HillClimbing.class, "Reverse-Delete MST (Hill-Climbing, very slow!)", MST, Slow),
		new AlgorithmInfo(AldousBroderUST.class, "Aldous-Broder UST (rather slow)", UST, Slow),
		new AlgorithmInfo(AldousBroderWilsonUST.class, "Houston UST (rather slow)", UST, Slow),
		new AlgorithmInfo(WilsonUSTRandomCell.class, "Wilson UST (random)", UST, Slow),
		new AlgorithmInfo(WilsonUSTRowsTopDown.class, "Wilson UST (row-wise, top-to-bottom)", UST),
		new AlgorithmInfo(WilsonUSTLeftToRightSweep.class, "Wilson UST (column-wise, left to right)", UST),
		new AlgorithmInfo(WilsonUSTRightToLeftSweep.class, "Wilson UST (column-wise, right to left)", UST),
		new AlgorithmInfo(WilsonUSTCollapsingWalls.class, "Wilson UST (column-wise, collapsing)", UST),
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
		new AlgorithmInfo(Armin.class, "Armin's Algorithm"), 
		new AlgorithmInfo(HuntAndKill.class, "Hunt-And-Kill"),
		new AlgorithmInfo(HuntAndKillRandom.class, "Hunt-And-Kill (random)"),
		new AlgorithmInfo(RecursiveDivision.class, "Recursive Division"),
		/*@formatter:on*/
	};

	public static final AlgorithmInfo[] PATHFINDER_ALGORITHMS = {
		/*@formatter:off*/
		new AlgorithmInfo(BreadthFirstSearch.class, "Breadth-First Search", BFS),
		new AlgorithmInfo(DepthFirstSearch.class, "Depth-First Search", DFS),
		new AlgorithmInfo(DepthFirstSearch2.class, "Depth-First Search (variation)", DFS), 
		new AlgorithmInfo(IDDFS.class, "Iterative Deepening DFS", DFS),
		new AlgorithmInfo(DijkstraSearch.class, "Uniform Cost (Dijkstra) Search", BFS), 
		new AlgorithmInfo(HillClimbingSearch.class, "Hill-Climbing Search", DFS, INFORMED),
		new AlgorithmInfo(BestFirstSearch.class, "Greedy Best-First Search", BFS, INFORMED),
		new AlgorithmInfo(AStarSearch.class, "A* Search", BFS, INFORMED),
		/*@formatter:on*/
	};

	public static Optional<AlgorithmInfo> find(AlgorithmInfo[] algorithms, Class<?> clazz) {
		return find(algorithms, alg -> alg.getAlgorithmClass() == clazz);
	}

	public static Optional<AlgorithmInfo> find(AlgorithmInfo[] algorithms, Predicate<AlgorithmInfo> predicate) {
		return Arrays.stream(algorithms).filter(Objects::nonNull).filter(predicate).findFirst();
	}

	private int gridWidth;
	private int gridHeight;
	private OrthogonalGrid grid;
	private int[] gridCellSizes;
	private int gridCellSize;
	private int passageWidthPercentage;
	private boolean generationAnimated;
	private boolean hidingControlsWhenRunning;
	private int delay;
	private GridPosition generationStart;
	private boolean floodFillAfterGeneration;
	private Metric metric;
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

	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public OrthogonalGrid getGrid() {
		return grid;
	}

	public void setGrid(OrthogonalGrid grid) {
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

	public Metric getMetric() {
		return metric;
	}

	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	public boolean isFloodFillAfterGeneration() {
		return floodFillAfterGeneration;
	}

	public void setFloodFillAfterGeneration(boolean floodFillAfterGeneration) {
		this.floodFillAfterGeneration = floodFillAfterGeneration;
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