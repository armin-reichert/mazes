package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;
import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.graph.grid.api.GridPosition;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.BFSAnimation;
import de.amr.graph.grid.ui.animation.DFSAnimation;
import de.amr.graph.pathfinder.impl.AStarSearch;
import de.amr.graph.pathfinder.impl.BestFirstSearch;
import de.amr.graph.pathfinder.impl.BreadthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch;
import de.amr.graph.pathfinder.impl.DepthFirstSearch2;
import de.amr.graph.pathfinder.impl.DijkstraSearch;
import de.amr.graph.pathfinder.impl.GraphSearch;
import de.amr.graph.pathfinder.impl.HillClimbingSearch;
import de.amr.graph.pathfinder.impl.IDDFS;
import de.amr.util.StopWatch;

/**
 * Animated execution of the selected path finding algorithm ("maze solver") on the current grid.
 * 
 * @author Armin Reichert
 */
public class SolveMazeAction extends AbstractAction {

	public SolveMazeAction() {
		putValue(NAME, "Solve");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().wndSettings.solverMenu.getSelectedAlgorithm().ifPresent(solver -> {
			app().enableUI(false);
			canvas().drawGrid();
			app().startWorkerThread(() -> {
				try {
					if (solver.isTagged(PathFinderTag.DFS)) {
						dfsSolverAnimation(solver);
					} else if (solver.isTagged(PathFinderTag.BFS)) {
						bfsSolverAnimation(solver);
					}
				} catch (Exception x) {
					x.printStackTrace();
					app().showMessage("Error during path finding: " + x.getMessage());
				} finally {
					app().enableUI(true);
				}
			});
		});
	}

	private void bfsSolverAnimation(AlgorithmInfo bfsSolverInfo) {

		if (bfsSolverInfo.getAlgorithmClass() == BreadthFirstSearch.class) {
			bfsSolver(new BreadthFirstSearch<>(model().getGrid()), bfsSolverInfo.getDescription(), false);
		}

		else if (bfsSolverInfo.getAlgorithmClass() == DijkstraSearch.class) {
			bfsSolver(new DijkstraSearch<>(model().getGrid(), edge -> 1), bfsSolverInfo.getDescription(), false);
		}

		else if (bfsSolverInfo.getAlgorithmClass() == BestFirstSearch.class) {
			bfsSolver(new BestFirstSearch<>(model().getGrid(), heuristics()), bfsSolverInfo.getDescription(), true);
		}

		else if (bfsSolverInfo.getAlgorithmClass() == AStarSearch.class) {
			bfsSolver(new AStarSearch<>(model().getGrid(), edge -> 1, metric()), bfsSolverInfo.getDescription(),
					true);
		}
	}

	private void dfsSolverAnimation(AlgorithmInfo dfsSolverInfo) {

		if (dfsSolverInfo.getAlgorithmClass() == DepthFirstSearch.class) {
			dfsSolver(new DepthFirstSearch<>(model().getGrid()), dfsSolverInfo.getDescription(), false);
		}

		else if (dfsSolverInfo.getAlgorithmClass() == DepthFirstSearch2.class) {
			dfsSolver(new DepthFirstSearch2<>(model().getGrid()), dfsSolverInfo.getDescription(), false);
		}

		else if (dfsSolverInfo.getAlgorithmClass() == IDDFS.class) {
			dfsSolver(new IDDFS<>(model().getGrid()), dfsSolverInfo.getDescription(), false);
		}

		else if (dfsSolverInfo.getAlgorithmClass() == HillClimbingSearch.class) {
			dfsSolver(new HillClimbingSearch<>(model().getGrid(), heuristics()), dfsSolverInfo.getDescription(),
					true);
		}
	}

	private void bfsSolver(GraphSearch<?, ?> solver, String solverName, boolean informed) {
		OrthogonalGrid grid = model().getGrid();
		BFSAnimation anim = new BFSAnimation(grid);
		anim.fnDelay = () -> model().getDelay();
		anim.fnPathColor = () -> model().getPathColor();
		StopWatch watch = new StopWatch();
		int source = grid.cell(model().getPathFinderSource());
		int target = grid.cell(model().getPathFinderTarget());
		watch.measure(() -> anim.run(canvas(), solver, source, target));
		app().showMessage(
				informed ? format("%s (%s): %.2f seconds.", solverName, model().getMetric(), watch.getSeconds())
						: format("%s: %.2f seconds.", solverName, watch.getSeconds()));
		anim.showPath(canvas(), solver, source, target);
	}

	private void dfsSolver(GraphSearch<?, ?> solver, String solverName, boolean informed) {
		OrthogonalGrid grid = model().getGrid();
		DFSAnimation anim = new DFSAnimation(grid);
		anim.fnDelay = () -> model().getDelay();
		anim.setPathColor(model().getPathColor());
		StopWatch watch = new StopWatch();
		int source = grid.cell(model().getPathFinderSource());
		int target = grid.cell(model().getPathFinderTarget());
		watch.measure(() -> anim.run(canvas(), solver, source, target));
		app().showMessage(
				informed ? format("%s (%s): %.2f seconds.", solverName, model().getMetric(), watch.getSeconds())
						: format("%s: %.2f seconds.", solverName, watch.getSeconds()));
	}

	private ToDoubleFunction<Integer> heuristics() {
		GridPosition targetPosition = model().getPathFinderTarget();
		int targetVertex = model().getGrid().cell(targetPosition);
		return v -> metric().applyAsDouble(v, targetVertex);
	}

	private ToDoubleBiFunction<Integer, Integer> metric() {
		switch (model().getMetric()) {
		case CHEBYSHEV:
			return model().getGrid()::chebyshev;
		case EUCLIDEAN:
			return model().getGrid()::euclidean2;
		case MANHATTAN:
			return model().getGrid()::manhattan;
		}
		throw new IllegalStateException();
	}
}