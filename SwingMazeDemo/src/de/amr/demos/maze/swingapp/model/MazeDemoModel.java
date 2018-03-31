package de.amr.demos.maze.swingapp.model;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.Tag.MST;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.Tag.Slow;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.Tag.SmallGridOnly;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.Tag.Traversal;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.Tag.UST;

import java.util.stream.Stream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingDFSAnimation;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.maze.alg.AldousBroderUST;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.BoruvkaMST;
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
import de.amr.easy.maze.alg.wilson.WilsonUSTMooreCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTNestedRectangles;
import de.amr.easy.maze.alg.wilson.WilsonUSTPeanoCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTRandomCell;
import de.amr.easy.maze.alg.wilson.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.alg.wilson.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.alg.wilson.WilsonUSTRowsTopDown;

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
		new AlgorithmInfo<>(RecursiveDFS.class, "Depth-First-Traversal (recursive, small grids only!)", Traversal, SmallGridOnly),
		new AlgorithmInfo<>(IterativeDFS.class, "Depth-First-Traversal (non-recursive)", Traversal),
		new AlgorithmInfo<>(RandomBFS.class, "Breadth-First-Traversal", Traversal),
		new AlgorithmInfo<>(KruskalMST.class, "Kruskal MST", MST),
		new AlgorithmInfo<>(PrimMST.class, "Prim MST", MST),
		new AlgorithmInfo<>(BoruvkaMST.class, "Boruvka MST (experimental, slow!)", MST, SmallGridOnly),
		new AlgorithmInfo<>(ReverseDeleteMST.class, "Reverse-Delete MST (experimental, slow!)", MST, SmallGridOnly),
		new AlgorithmInfo<>(AldousBroderUST.class, "Aldous-Broder UST (slow!)", UST, Slow),
		new AlgorithmInfo<>(WilsonUSTRandomCell.class, "Wilson UST (random, slow!)", UST, Slow),
		new AlgorithmInfo<>(WilsonUSTRowsTopDown.class, "Wilson UST (row-wise top-to-bottom)", UST),
		new AlgorithmInfo<>(WilsonUSTLeftToRightSweep.class, "Wilson UST (column-wise left to right)", UST),
		new AlgorithmInfo<>(WilsonUSTRightToLeftSweep.class, "Wilson UST (column-wise right to left)", UST),
		new AlgorithmInfo<>(WilsonUSTCollapsingWalls.class, "Wilson UST (column-wise collapsing)", UST),
		new AlgorithmInfo<>(WilsonUSTCollapsingRectangle.class, "Wilson UST (collapsing rectangle)", UST),
		new AlgorithmInfo<>(WilsonUSTExpandingCircle.class, "Wilson UST (expanding circle)", UST),
		new AlgorithmInfo<>(WilsonUSTCollapsingCircle.class, "Wilson UST (collapsing circle)", UST),
		new AlgorithmInfo<>(WilsonUSTExpandingCircles.class, "Wilson UST (expanding circles)", UST),
		new AlgorithmInfo<>(WilsonUSTExpandingSpiral.class, "Wilson UST (expanding spiral)", UST),
		new AlgorithmInfo<>(WilsonUSTExpandingRectangle.class, "Wilson UST (expanding rectangle)", UST),
		new AlgorithmInfo<>(WilsonUSTNestedRectangles.class, "Wilson UST (nested rectangles)", UST),
		new AlgorithmInfo<>(WilsonUSTRecursiveCrosses.class, "Wilson UST (recursive crosses)", UST),
		new AlgorithmInfo<>(WilsonUSTHilbertCurve.class, "Wilson UST (Hilbert curve)", UST),
		new AlgorithmInfo<>(WilsonUSTMooreCurve.class, "Wilson UST (Moore curve)", UST),
		new AlgorithmInfo<>(WilsonUSTPeanoCurve.class, "Wilson UST (Peano curve)", UST),
		new AlgorithmInfo<>(BinaryTree.class, "Binary Tree (row-wise, top-to-bottom"),
		new AlgorithmInfo<>(BinaryTreeRandom.class, "Binary Tree (random)"), 
		new AlgorithmInfo<>(Sidewinder.class, "Sidewinder (Slow!)", SmallGridOnly),
		new AlgorithmInfo<>(Eller.class, "Eller's Algorithm"), 
		new AlgorithmInfo<>(EllerInsideOut.class, "Armin's Algorithm"), 
		new AlgorithmInfo<>(HuntAndKill.class, "Hunt-And-Kill"),
		new AlgorithmInfo<>(HuntAndKillRandom.class, "Hunt-And-Kill (random)"),
		new AlgorithmInfo<>(GrowingTree.class, "Growing Tree", Slow),
		new AlgorithmInfo<>(RecursiveDivision.class, "Recursive Division"),
		/*@formatter:on*/
	};

	public Stream<AlgorithmInfo<?>> algorithms() {
		return Stream.of(ALGORITHMS);
	}

	public static final AlgorithmInfo<?>[] PATHFINDER_ALGORITHMS = {
			new AlgorithmInfo<>(SwingDFSAnimation.class, "Depth-First-Search"),
			new AlgorithmInfo<>(SwingBFSAnimation.class, "Breadth-First-Search"), };

	private ObservableGrid<TraversalState, Integer> grid;
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

	public ObservableGrid<TraversalState, Integer> getGrid() {
		return grid;
	}

	public void setGrid(ObservableGrid<TraversalState, Integer> grid) {
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
