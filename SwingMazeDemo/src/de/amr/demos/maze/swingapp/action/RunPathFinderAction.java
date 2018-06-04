package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.PathFinderTag.Euclidian;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.Manhattan;
import static de.amr.easy.util.GridUtils.byEuclidianDistFrom;
import static de.amr.easy.util.GridUtils.byManhattanDistFrom;
import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingDFSAnimation;

/**
 * Action for running the selected path finding algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunPathFinderAction extends MazeDemoAction {

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

	protected void runPathFinder(AlgorithmInfo pathFinderInfo) {
		final ObservableGrid<TraversalState, Integer> grid = app.model.getGrid();
		final ObservingGridCanvas canvas = app.getCanvas();
		final int source = grid.cell(app.model.getPathFinderSource());
		final int target = grid.cell(app.model.getPathFinderTarget());

		if (pathFinderInfo.getAlgorithmClass() == DepthFirstTraversal2.class) {
			SwingDFSAnimation animator = new SwingDFSAnimation(grid);
			animator.setPathColor(app.model.getPathColor());
			watch.measure(() -> animator.run(canvas, new DepthFirstTraversal2(grid), source, target));
			app.showMessage(format("Depth-first search: %.6f seconds.", watch.getSeconds()));
			return;
		}

		if (pathFinderInfo.getAlgorithmClass() == HillClimbing.class) {
			SwingDFSAnimation animator = new SwingDFSAnimation(grid);
			animator.setPathColor(app.model.getPathColor());
			if (pathFinderInfo.isTagged(Manhattan)) {
				watch.measure(() -> animator.run(canvas, new HillClimbing(grid, byManhattanDistFrom(grid, target).reversed()),
						source, target));
				app.showMessage(format("Hill Climbing (Manhattan): %.6f seconds.", watch.getSeconds()));
				return;
			}
			if (pathFinderInfo.isTagged(Euclidian)) {
				watch.measure(() -> animator.run(canvas, new HillClimbing(grid, byEuclidianDistFrom(grid, target).reversed()),
						source, target));
				app.showMessage(format("Hill Climbing (Euclidian): %.6f seconds.", watch.getSeconds()));
				return;
			}
		}

		if (pathFinderInfo.getAlgorithmClass() == BreadthFirstTraversal.class) {
			SwingBFSAnimation animator = new SwingBFSAnimation(grid, canvas);
			animator.setPathColor(app.model.getPathColor());
			BreadthFirstTraversal bfs = new BreadthFirstTraversal(grid);
			watch.measure(() -> animator.run(bfs, source, target));
			animator.showPath(bfs, target);
			app.showMessage(format("Breadth-first search: %.6f seconds.", watch.getSeconds()));
			return;
		}

		if (pathFinderInfo.getAlgorithmClass() == BestFirstTraversal.class) {
			SwingBFSAnimation animator = new SwingBFSAnimation(grid, canvas);
			animator.setPathColor(app.model.getPathColor());
			if (pathFinderInfo.isTagged(Manhattan)) {
				BestFirstTraversal best = new BestFirstTraversal(grid, byManhattanDistFrom(grid, target));
				watch.measure(() -> animator.run(best, source, target));
				animator.showPath(best, target);
				app.showMessage(format("Best-first search (Manhattan): %.6f seconds.", watch.getSeconds()));
				return;
			}
			if (pathFinderInfo.isTagged(Euclidian)) {
				BestFirstTraversal best = new BestFirstTraversal(grid, byEuclidianDistFrom(grid, target));
				watch.measure(() -> animator.run(best, source, target));
				animator.showPath(best, target);
				app.showMessage(format("Best-first search (Euclidian): %.6f seconds.", watch.getSeconds()));
				return;
			}
		}
	}
}