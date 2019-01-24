package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.graph.grid.ui.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.graph.grid.ui.animation.DepthFirstTraversalAnimation;
import de.amr.easy.graph.pathfinder.api.TraversalState;
import de.amr.easy.graph.pathfinder.impl.AStarPathFinder;
import de.amr.easy.graph.pathfinder.impl.BestFirstSearchPathFinder;
import de.amr.easy.graph.pathfinder.impl.BreadthFirstSearchPathFinder;
import de.amr.easy.graph.pathfinder.impl.DepthFirstSearchPathFinder;
import de.amr.easy.graph.pathfinder.impl.DepthFirstSearchPathFinder2;
import de.amr.easy.graph.pathfinder.impl.DijkstraPathFinder;
import de.amr.easy.graph.pathfinder.impl.HillClimbingPathFinder;
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
		int source = grid.cell(app.model.getPathFinderSource());
		int target = grid.cell(app.model.getPathFinderTarget());

		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.fnPathColor = () -> app.model.getPathColor();

		if (solver.getAlgorithmClass() == BreadthFirstSearchPathFinder.class) {
			BreadthFirstSearchPathFinder<?, ?> bfs = new BreadthFirstSearchPathFinder<>(grid);
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), bfs, source, target));
			app.showMessage(format("Breadth-first search: %.2f seconds.", watch.getSeconds()));
			anim.showPath(app.wndDisplayArea.getCanvas(), bfs, target);
		}

		else if (solver.getAlgorithmClass() == DijkstraPathFinder.class) {
			DijkstraPathFinder<TraversalState, Integer> dijkstra = new DijkstraPathFinder<>(grid, edge -> 1);
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), dijkstra, source, target));
			app.showMessage(format("Dijkstra search: %.2f seconds.", watch.getSeconds()));
			anim.showPath(app.wndDisplayArea.getCanvas(), dijkstra, target);
		}

		else if (solver.getAlgorithmClass() == BestFirstSearchPathFinder.class) {
			getHeuristics(solver, grid, target).ifPresent(h -> {
				BestFirstSearchPathFinder<?, ?, Integer> best = new BestFirstSearchPathFinder<>(grid, h);
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), best, source, target));
				app.showMessage(
						format("Best-first search (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
				anim.showPath(app.wndDisplayArea.getCanvas(), best, target);
			});
		}

		else if (solver.getAlgorithmClass() == AStarPathFinder.class) {
			getCostFunction(solver, grid).ifPresent(cost -> {
				AStarPathFinder<TraversalState, Integer> astar = new AStarPathFinder<>(grid, edge -> 1, cost);
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), astar, source, target));
				app.showMessage(
						format("A* search (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
				anim.showPath(app.wndDisplayArea.getCanvas(), astar, target);
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

		if (solver.getAlgorithmClass() == DepthFirstSearchPathFinder.class) {
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new DepthFirstSearchPathFinder(grid),
					source, target));
			app.showMessage(format("Depth-first search: %.2f seconds.", watch.getSeconds()));
		}

		else if (solver.getAlgorithmClass() == DepthFirstSearchPathFinder2.class) {
			watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new DepthFirstSearchPathFinder2(grid),
					source, target));
			app.showMessage(format("Depth-first search: %.2f seconds.", watch.getSeconds()));
		}

		else if (solver.getAlgorithmClass() == HillClimbingPathFinder.class) {
			getHeuristics(solver, grid, target).ifPresent(h -> {
				watch.measure(() -> anim.run(app.wndDisplayArea.getCanvas(), new HillClimbingPathFinder<>(grid, h),
						source, target));
				app.showMessage(
						format("Hill Climbing (%s): %.2f seconds.", getHeuristicsName(solver), watch.getSeconds()));
			});
		}
	}

	private String getHeuristicsName(AlgorithmInfo solver) {
		return app.model.getHeuristics().toString();
	}

	private Optional<Function<Integer, Integer>> getHeuristics(AlgorithmInfo solver, OrthogonalGrid grid,
			int target) {
		Optional<BiFunction<Integer, Integer, Integer>> cost = getCostFunction(solver, grid);
		Function<Integer, Integer> h = cost.isPresent() ? v -> cost.get().apply(v, target) : null;
		return Optional.ofNullable(h);
	}

	private Optional<BiFunction<Integer, Integer, Integer>> getCostFunction(AlgorithmInfo solver,
			OrthogonalGrid grid) {
		BiFunction<Integer, Integer, Integer> cost = null;
		switch (app.model.getHeuristics()) {
		case CHEBYSHEV:
			cost = (u, v) -> grid.chebyshev(u, v);
			break;
		case EUCLIDEAN:
			cost = (u, v) -> grid.euclidean2(u, v);
			break;
		case MANHATTAN:
			cost = (u, v) -> grid.manhattan(u, v);
			break;
		}
		return Optional.ofNullable(cost);
	}
}