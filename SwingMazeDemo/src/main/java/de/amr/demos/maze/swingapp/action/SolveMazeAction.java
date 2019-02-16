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
					runSolverAnimation(solver);
				} catch (Exception x) {
					x.printStackTrace();
					app().showMessage("Error during path finding: " + x.getMessage());
				} finally {
					app().enableUI(true);
				}
			});
		});
	}

	private void runSolverAnimation(AlgorithmInfo solverInfo) {

		if (solverInfo.getAlgorithmClass() == BreadthFirstSearch.class) {
			runSolver(new BreadthFirstSearch<>(model().getGrid()), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == DijkstraSearch.class) {
			runSolver(new DijkstraSearch<>(model().getGrid(), edge -> 1), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == BestFirstSearch.class) {
			runSolver(new BestFirstSearch<>(model().getGrid(), heuristics()), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == AStarSearch.class) {
			runSolver(new AStarSearch<>(model().getGrid(), edge -> 1, metric()), solverInfo);
		}

		if (solverInfo.getAlgorithmClass() == DepthFirstSearch.class) {
			runSolver(new DepthFirstSearch<>(model().getGrid()), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == DepthFirstSearch2.class) {
			runSolver(new DepthFirstSearch2<>(model().getGrid()), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == IDDFS.class) {
			runSolver(new IDDFS<>(model().getGrid()), solverInfo);
		}

		else if (solverInfo.getAlgorithmClass() == HillClimbingSearch.class) {
			runSolver(new HillClimbingSearch<>(model().getGrid(), heuristics()), solverInfo);
		}
	}

	private void runSolver(GraphSearch<?, ?> solver, AlgorithmInfo solverInfo) {
		OrthogonalGrid grid = model().getGrid();
		int source = grid.cell(model().getPathFinderSource());
		int target = grid.cell(model().getPathFinderTarget());
		boolean informed = solverInfo.isTagged(PathFinderTag.INFORMED);
		StopWatch watch = new StopWatch();
		if (solverInfo.isTagged(PathFinderTag.BFS)) {
			BFSAnimation anim = new BFSAnimation(canvas());
			anim.fnDelay = () -> model().getDelay();
			anim.fnPathColor = () -> model().getPathColor();
			watch.measure(() -> anim.run(solver, source, target));
			anim.showPath(solver, source, target);
		} else if (solverInfo.isTagged(PathFinderTag.DFS)) {
			DFSAnimation anim = new DFSAnimation(grid);
			anim.fnDelay = () -> model().getDelay();
			anim.setPathColor(model().getPathColor());
			watch.measure(() -> anim.run(canvas(), solver, source, target));
		}
		app().showMessage(informed
				? format("%s (%s): %.2f seconds.", solverInfo.getDescription(), model().getMetric(),
						watch.getSeconds())
				: format("%s: %.2f seconds.", solverInfo.getDescription(), watch.getSeconds()));
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