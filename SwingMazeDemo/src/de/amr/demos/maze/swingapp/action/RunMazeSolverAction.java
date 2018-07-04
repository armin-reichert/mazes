package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.PathFinderTag.CHEBYSHEV;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.EUCLIDEAN;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;
import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.impl.traversal.HillClimbing;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.maze.alg.core.OrthogonalGrid;
import de.amr.easy.util.StopWatch;

/**
 * Action for running the selected path finding ("maze solver") algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunMazeSolverAction extends AbstractAction {

	private final MazeDemoApp app;
	private final StopWatch watch = new StopWatch();

	public RunMazeSolverAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Solve");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.solverMenu.getSelectedAlgorithm().ifPresent(solver -> {
			app.enableUI(false);
			app.wndDisplayArea.getCanvas().drawGrid();
			app.startWorkerThread(() -> {
				try {
					if (solver.isTagged(PathFinderTag.DFS)) {
						runDFSSolverAnimation(solver);
					} else if (solver.isTagged(PathFinderTag.BFS)) {
						runBFSSolverAnimation(solver);
					}
				} catch (Exception x) {
					x.printStackTrace();
				} finally {
					app.enableUI(true);
				}
			});
		});
	}

	private void runBFSSolverAnimation(AlgorithmInfo solver) {
		OrthogonalGrid grid = app.model.getGrid();
		int src = grid.cell(app.model.getPathFinderSource());
		int tgt = grid.cell(app.model.getPathFinderTarget());

		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.fnPathColor = () -> app.model.getPathColor();

		if (solver.getAlgorithmClass() == BreadthFirstTraversal.class) {
			BreadthFirstTraversal<?, ?> bfs = new BreadthFirstTraversal<>(grid);
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), bfs, src, tgt));
			app.showMessage(format("Breadth-first search: %.2f seconds.", watch.getSeconds()));
			anim.showPath(app.wndDisplayArea.getCanvas(), bfs, tgt);
		}

		else if (solver.getAlgorithmClass() == BestFirstTraversal.class) {
			getHeuristics(solver, grid, tgt).ifPresent(h -> {
				BestFirstTraversal<?, ?, Integer> best = new BestFirstTraversal<>(grid, h);
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), best, src, tgt));
				app.showMessage(format("Best-first search (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
				anim.showPath(app.wndDisplayArea.getCanvas(), best, tgt);
			});
		}

		else if (solver.getAlgorithmClass() == AStarTraversal.class) {
			getCostFunction(solver, grid).ifPresent(cost -> {
				AStarTraversal<TraversalState> astar = new AStarTraversal<>(grid, cost);
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), astar, src, tgt));
				app.showMessage(format("A* search (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
				anim.showPath(app.wndDisplayArea.getCanvas(), astar, tgt);
			});
		}
	}

	private void runDFSSolverAnimation(AlgorithmInfo solver) {
		OrthogonalGrid grid = app.model.getGrid();
		int source = grid.cell(app.model.getPathFinderSource());
		int target = grid.cell(app.model.getPathFinderTarget());

		DepthFirstTraversalAnimation anim = new DepthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.setPathColor(app.model.getPathColor());

		if (solver.getAlgorithmClass() == DepthFirstTraversal.class) {
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new DepthFirstTraversal(grid), source, target));
			app.showMessage(format("Depth-first search: %.2f seconds.", watch.getSeconds()));
		}

		else if (solver.getAlgorithmClass() == DepthFirstTraversal2.class) {
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new DepthFirstTraversal2(grid), source, target));
			app.showMessage(format("Depth-first search: %.2f seconds.", watch.getSeconds()));
		}

		else if (solver.getAlgorithmClass() == HillClimbing.class) {
			getHeuristics(solver, grid, target).ifPresent(h -> {
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new HillClimbing<>(grid, h), source, target));
				app.showMessage(format("Hill Climbing (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
			});
		}
	}

	private String getHeuristicsName(AlgorithmInfo solver) {
		if (solver.isTagged(MANHATTAN)) {
			return "Manhattan";
		}
		if (solver.isTagged(EUCLIDEAN)) {
			return "Euclidean";
		}
		if (solver.isTagged(EUCLIDEAN)) {
			return "Chebyshev";
		}
		return "";
	}

	private Optional<Function<Integer, Integer>> getHeuristics(AlgorithmInfo solver, OrthogonalGrid grid, int target) {
		Optional<BiFunction<Integer, Integer, Integer>> cost = getCostFunction(solver, grid);
		Function<Integer, Integer> h = cost.isPresent() ? v -> cost.get().apply(v, target) : null;
		return Optional.ofNullable(h);
	}

	private Optional<BiFunction<Integer, Integer, Integer>> getCostFunction(AlgorithmInfo solver, OrthogonalGrid grid) {
		BiFunction<Integer, Integer, Integer> cost = null;
		if (solver.isTagged(CHEBYSHEV)) {
			cost = (u, v) -> grid.chebyshev(u, v);
		} else if (solver.isTagged(EUCLIDEAN)) {
			cost = (u, v) -> grid.euclidean2(u, v);
		} else if (solver.isTagged(MANHATTAN)) {
			cost = (u, v) -> grid.manhattan(u, v);
		}
		return Optional.ofNullable(cost);
	}
}