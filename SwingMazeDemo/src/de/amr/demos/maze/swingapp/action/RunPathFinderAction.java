package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.PathFinderTag.CHEBYSHEV;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.EUCLIDEAN;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;
import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.function.Function;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.animation.ObservingGridCanvas;

/**
 * Action for running the selected path finding algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunPathFinderAction extends MazeDemoAction {

	private class Heuristics {

		private final String name;
		private final Function<Integer, Integer> fn;

		public Heuristics(String name, Function<Integer, Integer> fn) {
			this.name = name;
			this.fn = fn;
		}
	}

	public RunPathFinderAction(MazeDemoApp app) {
		super(app, "Solve");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(!app.model.isHidingControlsWhenRunning());
		app.wndMaze.setVisible(true);
		enableUI(false);
		app.startTask(() -> {
			try {
				app.getCanvas().drawGrid();
				app.wndSettings.getPathFinderMenu().selectedAlgorithm().ifPresent(this::runPathFinder);
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				enableUI(true);
				app.wndSettings.setVisible(true);
				app.wndSettings.requestFocus();
			}
		});
	}

	private void runPathFinder(AlgorithmInfo pathFinderInfo) {
		final ObservableGrid<TraversalState, Integer> grid = app.model.getGrid();
		final ObservingGridCanvas canvas = app.getCanvas();
		final int src = grid.cell(app.model.getPathFinderSource());
		final int tgt = grid.cell(app.model.getPathFinderTarget());

		if (pathFinderInfo.getAlgorithmClass() == DepthFirstTraversal2.class) {
			DepthFirstTraversalAnimation<?> anim = new DepthFirstTraversalAnimation<>(grid);
			anim.setPathColor(app.model.getPathColor());
			watch.measure(() -> anim.run(canvas, new DepthFirstTraversal2<>(grid), src, tgt));
			app.showMessage(format("Depth-first search: %.6f seconds.", watch.getSeconds()));
			return;
		}

		if (pathFinderInfo.getAlgorithmClass() == HillClimbing.class) {
			Heuristics heuristics;
			if (pathFinderInfo.isTagged(CHEBYSHEV)) {
				heuristics = new Heuristics("Chebyshev", v -> grid.chebyshev(v, tgt));
			} else if (pathFinderInfo.isTagged(EUCLIDEAN)) {
				heuristics = new Heuristics("Euclidean", v -> grid.euclidean2(v, tgt));
			} else if (pathFinderInfo.isTagged(MANHATTAN)) {
				heuristics = new Heuristics("Manhattan", v -> grid.manhattan(v, tgt));
			} else {
				return;
			}
			DepthFirstTraversalAnimation<?> anim = new DepthFirstTraversalAnimation<>(grid);
			anim.setPathColor(app.model.getPathColor());
			watch.measure(() -> anim.run(canvas, new HillClimbing<>(grid, heuristics.fn), src, tgt));
			app.showMessage(format("Hill Climbing (%s): %.6f seconds.", heuristics.name, watch.getSeconds()));
			return;
		}

		if (pathFinderInfo.getAlgorithmClass() == BreadthFirstTraversal.class) {
			BreadthFirstTraversal<ObservableGrid<TraversalState, Integer>> bfs = new BreadthFirstTraversal<>(grid);
			BreadthFirstTraversalAnimation<?> anim = new BreadthFirstTraversalAnimation<>(grid);
			anim.setPathColor(app.model.getPathColor());
			watch.measure(() -> anim.run(canvas, bfs, src, tgt));
			anim.showPath(canvas, bfs, tgt);
			app.showMessage(format("Breadth-first search: %.6f seconds.", watch.getSeconds()));
			return;
		}

		if (pathFinderInfo.getAlgorithmClass() == BestFirstTraversal.class) {
			Heuristics heuristics;
			if (pathFinderInfo.isTagged(CHEBYSHEV)) {
				heuristics = new Heuristics("Chebyshev", v -> grid.chebyshev(v, tgt));
			} else if (pathFinderInfo.isTagged(EUCLIDEAN)) {
				heuristics = new Heuristics("Euclidean", v -> grid.euclidean2(v, tgt));
			} else if (pathFinderInfo.isTagged(MANHATTAN)) {
				heuristics = new Heuristics("Manhattan", v -> grid.manhattan(v, tgt));
			} else {
				return;
			}
			BestFirstTraversal<ObservableGrid<TraversalState, Integer>, Integer> best = new BestFirstTraversal<>(grid,
					heuristics.fn);
			BreadthFirstTraversalAnimation<?> anim = new BreadthFirstTraversalAnimation<>(grid);
			anim.setPathColor(app.model.getPathColor());
			watch.measure(() -> anim.run(canvas, best, src, tgt));
			anim.showPath(canvas, best, tgt);
			app.showMessage(format("Best-first search (%s): %.6f seconds.", heuristics.name, watch.getSeconds()));
			return;
		}
	}
}