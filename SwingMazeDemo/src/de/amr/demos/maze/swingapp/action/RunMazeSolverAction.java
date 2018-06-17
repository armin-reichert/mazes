package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.PathFinderTag.CHEBYSHEV;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.EUCLIDEAN;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;
import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.Optional;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeGrid;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.demos.maze.swingapp.model.VertexCostHeuristics;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.impl.traversal.HillClimbing;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.util.StopWatch;

/**
 * Action for running the selected path finding ("maze solver") algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunMazeSolverAction extends MazeDemoAction {

	private final StopWatch watch = new StopWatch();

	public RunMazeSolverAction(MazeDemoApp app) {
		super(app, "Solve");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.getSolverMenu().getSelectedAlgorithm().ifPresent(solver -> {
			app.enableUI(false);
			app.getCanvas().drawGrid();
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
		MazeGrid grid = app.model.getGrid();
		int src = grid.cell(app.model.getPathFinderSource());
		int tgt = grid.cell(app.model.getPathFinderTarget());

		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.setPathColor(app.model.getPathColor());

		if (solver.getAlgorithmClass() == BreadthFirstTraversal.class) {
			BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid);
			watch.measure(() -> anim.run(app.getCanvas(), bfs, src, tgt));
			app.showMessage(format("Breadth-first search: %.2f seconds.", watch.getSeconds()));
			anim.showPath(app.getCanvas(), bfs, tgt);
		}

		else if (solver.getAlgorithmClass() == BestFirstTraversal.class) {
			findHeuristics(solver, grid, tgt).ifPresent(h -> {
				BestFirstTraversal<Integer> best = new BestFirstTraversal<>(grid, h.getCostFunction());
				watch.measure(() -> anim.run(app.getCanvas(), best, src, tgt));
				app.showMessage(format("Best-first search (%s): %.2f seconds.", h.getName(), watch.getSeconds()));
				anim.showPath(app.getCanvas(), best, tgt);
			});
		}
	}

	private void runDFSSolverAnimation(AlgorithmInfo solver) {
		MazeGrid grid = app.model.getGrid();
		int src = grid.cell(app.model.getPathFinderSource());
		int tgt = grid.cell(app.model.getPathFinderTarget());

		DepthFirstTraversalAnimation anim = new DepthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> app.model.getDelay();
		anim.setPathColor(app.model.getPathColor());

		if (solver.getAlgorithmClass() == DepthFirstTraversal2.class) {
			watch.measure(() -> anim.run(app.getCanvas(), new DepthFirstTraversal2(grid), src, tgt));
			app.showMessage(format("Depth-first search: %.2f seconds.", watch.getSeconds()));
		}

		else if (solver.getAlgorithmClass() == HillClimbing.class) {
			findHeuristics(solver, grid, tgt).ifPresent(h -> {
				watch.measure(() -> anim.run(app.getCanvas(), new HillClimbing<>(grid, h.getCostFunction()), src, tgt));
				app.showMessage(format("Hill Climbing (%s): %.2f seconds.", h.getName(), watch.getSeconds()));
			});
		}
	}

	private Optional<VertexCostHeuristics> findHeuristics(AlgorithmInfo solver, MazeGrid grid, int tgt) {
		VertexCostHeuristics h = null;
		if (solver.isTagged(CHEBYSHEV)) {
			h = new VertexCostHeuristics("Chebyshev", v -> grid.chebyshev(v, tgt));
		} else if (solver.isTagged(EUCLIDEAN)) {
			h = new VertexCostHeuristics("Euclidean", v -> grid.euclidean2(v, tgt));
		} else if (solver.isTagged(MANHATTAN)) {
			h = new VertexCostHeuristics("Manhattan", v -> grid.manhattan(v, tgt));
		}
		return Optional.ofNullable(h);
	}
}