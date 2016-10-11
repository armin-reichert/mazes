package de.amr.mazes.swing.model;

import static de.amr.mazes.swing.model.MazeDemoModel.Tag.MST;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.Slow;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.SmallGridOnly;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.Traversal;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.UST;

import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableDataGrid;
import de.amr.easy.maze.algorithms.AldousBroderUST;
import de.amr.easy.maze.algorithms.BinaryTree;
import de.amr.easy.maze.algorithms.BinaryTreeRandom;
import de.amr.easy.maze.algorithms.Eller;
import de.amr.easy.maze.algorithms.EllerInsideOut;
import de.amr.easy.maze.algorithms.HuntAndKill;
import de.amr.easy.maze.algorithms.IterativeDFS;
import de.amr.easy.maze.algorithms.KruskalMST;
import de.amr.easy.maze.algorithms.PrimMST;
import de.amr.easy.maze.algorithms.RandomBFS;
import de.amr.easy.maze.algorithms.RecursiveDFS;
import de.amr.easy.maze.algorithms.RecursiveDivision;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTCollapsingCircle;
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
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.algorithms.wilson.WilsonUSTRowsTopDown;
import de.amr.mazes.samples.maze.HuntAndKillRandom;
import de.amr.mazes.swing.rendering.BFSAnimation;
import de.amr.mazes.swing.rendering.DFSAnimation;

/**
 * Data model of the maze demo application.
 * 
 * @author Armin Reichert
 */
public class MazeDemoModel {

	public enum Tag {
		Traversal, MST, UST, Slow, SmallGridOnly;
	};

	private static final AlgorithmInfo<?>[] ALGORITHMS = {
			/*@formatter:off*/
			new AlgorithmInfo<>(IterativeDFS.class, "Depth-First-Traversal (iterative)", Traversal),
			new AlgorithmInfo<>(RecursiveDFS.class, "Depth-First-Traversal (recursive), small grids only!)", Traversal, SmallGridOnly),
			new AlgorithmInfo<>(RandomBFS.class, "Breadth-First-Traversal", Traversal),
			new AlgorithmInfo<>(KruskalMST.class, "Kruskal MST", MST),
			new AlgorithmInfo<>(PrimMST.class, "Prim MST", MST),
			new AlgorithmInfo<>(AldousBroderUST.class, "Aldous-Broder UST (slow!)", UST, Slow),
			new AlgorithmInfo<>(WilsonUSTRandomCell.class, "Wilson UST (random, slow!)", UST, Slow),
			new AlgorithmInfo<>(WilsonUSTRowsTopDown.class, "Wilson UST (row-by-row)", UST),
			new AlgorithmInfo<>(WilsonUSTLeftToRightSweep.class, "Wilson UST (left to right)", UST),
			new AlgorithmInfo<>(WilsonUSTRightToLeftSweep.class, "Wilson UST (right to left)", UST),
			new AlgorithmInfo<>(WilsonUSTCollapsingWalls.class, "Wilson UST (collapsing horizontally)", UST),
			new AlgorithmInfo<>(WilsonUSTExpandingCircle.class, "Wilson UST (circle, expanding)", UST),
			new AlgorithmInfo<>(WilsonUSTCollapsingCircle.class, "Wilson UST (circle, collapsing)", UST),
			new AlgorithmInfo<>(WilsonUSTExpandingCircles.class, "Wilson UST (circles, expanding)", UST),
			new AlgorithmInfo<>(WilsonUSTExpandingSpiral.class, "Wilson UST (spiral, expanding)", UST),
			new AlgorithmInfo<>(WilsonUSTExpandingRectangle.class, "Wilson UST (rectangle, expanding)", UST),
			new AlgorithmInfo<>(WilsonUSTNestedRectangles.class, "Wilson UST (rectangles, nested)", UST),
			new AlgorithmInfo<>(WilsonUSTHilbertCurve.class, "Wilson UST (Hilbert curve)", UST),
			new AlgorithmInfo<>(WilsonUSTPeanoCurve.class, "Wilson UST (Peano curve)", UST),
			new AlgorithmInfo<>(HuntAndKill.class, "Hunt-And-Kill"),
			new AlgorithmInfo<>(HuntAndKillRandom.class, "Hunt-And-Kill (random)"),
			new AlgorithmInfo<>(RecursiveDivision.class, "Recursive Division"),
			new AlgorithmInfo<>(Eller.class, "Eller's Algorithm"), 
			new AlgorithmInfo<>(EllerInsideOut.class, "Armin's Algorithm"), 
			new AlgorithmInfo<>(BinaryTree.class, "Binary Tree"),
			new AlgorithmInfo<>(BinaryTreeRandom.class, "Binary Tree (random)"), 
			/*@formatter:on*/
	};

	public Stream<AlgorithmInfo<?>> algorithms() {
		return Stream.of(ALGORITHMS);
	}

	public static final AlgorithmInfo<?>[] PATHFINDER_ALGORITHMS = {
			new AlgorithmInfo<>(DFSAnimation.class, "Depth-First-Search"),
			new AlgorithmInfo<>(BFSAnimation.class, "Breadth-First-Search"), };

	private ObservableDataGrid<TraversalState> grid;
	private int[] gridCellSizes;
	private int gridCellSize;
	private int passageThicknessPct;
	private boolean generationAnimated;
	private boolean hidingControlsWhenRunning;
	private boolean longestPathHighlighted;
	private int delay;
	private GridPosition generationStart;
	private GridPosition pathFinderStart;
	private GridPosition pathFinderTarget;

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

	public int getPassageThicknessPct() {
		return passageThicknessPct;
	}

	public void setPassageThicknessPct(int percent) {
		this.passageThicknessPct = percent;
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

	public boolean isLongestPathHighlighted() {
		return longestPathHighlighted;
	}

	public void setLongestPathHighlighted(boolean longestPathHighlighted) {
		this.longestPathHighlighted = longestPathHighlighted;
	}

	public ObservableDataGrid<TraversalState> getGrid() {
		return grid;
	}

	public void setGrid(ObservableDataGrid<TraversalState> grid) {
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
}
