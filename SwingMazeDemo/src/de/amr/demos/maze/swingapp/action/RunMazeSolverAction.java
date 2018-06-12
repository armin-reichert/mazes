package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.PathFinderTag.CHEBYSHEV;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.EUCLIDEAN;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;
import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.function.Function;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeGrid;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;

/**
 * Action for running the selected path finding ("maze solver") algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunMazeSolverAction extends MazeDemoAction {

	private class Heuristics {

		private final String name;
		private final Function<Integer, Integer> fn;

		public Heuristics(String name, Function<Integer, Integer> fn) {
			this.name = name;
			this.fn = fn;
		}
	}

	public RunMazeSolverAction(MazeDemoApp app) {
		super(app, "Solve");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(!app.model.isHidingControlsWhenRunning());
		app.wndCanvas.setVisible(true);
		enableUI(false);
		app.startTask(() -> {
			try {
				app.getCanvas().drawGrid();
				app.wndSettings.getSolverMenu().getSelectedAlgorithm().ifPresent(this::runSolverAnimation);
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				enableUI(true);
				app.wndSettings.setVisible(true);
				app.wndSettings.requestFocus();
			}
		});
	}

	private void runSolverAnimation(AlgorithmInfo solver) {
		if (solver.isTagged(PathFinderTag.DFS)) {
			runDFSSolverAnimation(solver);
		} else if (solver.isTagged(PathFinderTag.BFS)) {
			runBFSSolverAnimation(solver);
		}
	}

	private void runBFSSolverAnimation(AlgorithmInfo solver) {
		MazeGrid grid = app.model.getGrid();
		int src = grid.cell(app.model.getPathFinderSource());
		int tgt = grid.cell(app.model.getPathFinderTarget());

		BreadthFirstTraversalAnimation<MazeGrid> anim = new BreadthFirstTraversalAnimation<>(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.setPathColor(app.model.getPathColor());

		if (solver.getAlgorithmClass() == BreadthFirstTraversal.class) {
			BreadthFirstTraversal<MazeGrid> bfs = new BreadthFirstTraversal<>(grid);
			watch.measure(() -> anim.run(app.getCanvas(), bfs, src, tgt));
			app.showMessage(format("Breadth-first search: %.6f seconds.", watch.getSeconds()));
			anim.showPath(app.getCanvas(), bfs, tgt);
		}

		if (solver.getAlgorithmClass() == BestFirstTraversal.class) {
			findHeuristics(solver, grid, tgt).ifPresent(heuristics -> {
				BestFirstTraversal<MazeGrid, Integer> best = new BestFirstTraversal<>(grid, heuristics.fn);
				watch.measure(() -> anim.run(app.getCanvas(), best, src, tgt));
				anim.showPath(app.getCanvas(), best, tgt);
				app.showMessage(format("Best-first search (%s): %.6f seconds.", heuristics.name, watch.getSeconds()));
			});
		}
	}

	private void runDFSSolverAnimation(AlgorithmInfo solver) {
		MazeGrid grid = app.model.getGrid();
		int src = grid.cell(app.model.getPathFinderSource());
		int tgt = grid.cell(app.model.getPathFinderTarget());

		DepthFirstTraversalAnimation<MazeGrid> dfsAnim = new DepthFirstTraversalAnimation<>(grid);
		dfsAnim.setPathColor(app.model.getPathColor());
		dfsAnim.fnDelay = () -> app.model.getDelay();

		if (solver.getAlgorithmClass() == DepthFirstTraversal2.class) {
			watch.measure(() -> dfsAnim.run(app.getCanvas(), new DepthFirstTraversal2<>(grid), src, tgt));
			app.showMessage(format("Depth-first search: %.6f seconds.", watch.getSeconds()));
		}

		if (solver.getAlgorithmClass() == HillClimbing.class) {
			findHeuristics(solver, grid, tgt).ifPresent(heuristics -> {
				watch.measure(() -> dfsAnim.run(app.getCanvas(), new HillClimbing<>(grid, heuristics.fn), src, tgt));
				app.showMessage(format("Hill Climbing (%s): %.6f seconds.", heuristics.name, watch.getSeconds()));
			});
		}
	}

	private Optional<Heuristics> findHeuristics(AlgorithmInfo solver, MazeGrid grid, int tgt) {
		if (solver.isTagged(CHEBYSHEV)) {
			return Optional.of(new Heuristics("Chebyshev", v -> grid.chebyshev(v, tgt)));
		}
		if (solver.isTagged(EUCLIDEAN)) {
			return Optional.of(new Heuristics("Euclidean", v -> grid.euclidean2(v, tgt)));
		}
		if (solver.isTagged(MANHATTAN)) {
			return Optional.of(new Heuristics("Manhattan", v -> grid.manhattan(v, tgt)));
		}
		return Optional.empty();
	}
}