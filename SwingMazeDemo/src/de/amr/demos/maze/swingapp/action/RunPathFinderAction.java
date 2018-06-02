package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.util.GridUtils.euclidianValuation;
import static de.amr.easy.util.GridUtils.manhattanValuation;
import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingDFSAnimation;

/**
 * Action for running the selected path finding algorithm on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunPathFinderAction extends MazeDemoAction {

	public RunPathFinderAction(MazeDemoApp app) {
		super(app, "Solve Maze");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		enableControls(false);
		app.startTask(() -> {
			try {
				app.getCanvas().drawGrid();
				app.settingsWindow.getPathFinderMenu().getSelectedAlgorithm().ifPresent(this::runPathFinder);
			} catch (Throwable x) {
				x.printStackTrace();
			} finally {
				enableControls(true);
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
			}
		});
	}

	protected void runPathFinder(AlgorithmInfo pathFinderInfo) {
		int source = app.model.getGrid().cell(app.model.getPathFinderSource());
		int target = app.model.getGrid().cell(app.model.getPathFinderTarget());
		if (pathFinderInfo.getAlgorithmClass() == SwingBFSAnimation.class) {
			SwingBFSAnimation bfsAnimation = new SwingBFSAnimation(app.model.getGrid(), app.getCanvas());
			bfsAnimation.setPathColor(app.model.getPathColor());
			if (pathFinderInfo.isTagged(PathFinderTag.BestFSManhattan)) {
				BestFirstTraversal best = new BestFirstTraversal(app.model.getGrid(),
						(u, v) -> manhattanValuation(app.model.getGrid(), target).apply(u, v));
				watch.runAndMeasure(() -> bfsAnimation.run(best, source, target));
				app.showMessage(format("BestFS (Manhattan) time: %.6f seconds.", watch.getSeconds()));
				bfsAnimation.showPath(best, target);
			} else if (pathFinderInfo.isTagged(PathFinderTag.BestFSEuclidian)) {
				BestFirstTraversal best = new BestFirstTraversal(app.model.getGrid(),
						(u, v) -> euclidianValuation(app.model.getGrid(), target).apply(u, v));
				watch.runAndMeasure(() -> bfsAnimation.run(best, source, target));
				app.showMessage(format("BestFS (Euclidian) time: %.6f seconds.", watch.getSeconds()));
				bfsAnimation.showPath(best, target);
			} else {
				BreadthFirstTraversal bfs = new BreadthFirstTraversal(app.model.getGrid());
				watch.runAndMeasure(() -> bfsAnimation.run(bfs, source, target));
				app.showMessage(format("BFS time: %.6f seconds.", watch.getSeconds()));
				bfsAnimation.showPath(bfs, target);
			}
		} else if (pathFinderInfo.getAlgorithmClass() == SwingDFSAnimation.class) {
			SwingDFSAnimation dfsAnimation = new SwingDFSAnimation(app.model.getGrid());
			dfsAnimation.setPathColor(app.model.getPathColor());
			if (pathFinderInfo.isTagged(PathFinderTag.HillClimbingManhattan)) {
				HillClimbing hillClimbing = new HillClimbing(app.model.getGrid(),
						(u, v) -> manhattanValuation(app.model.getGrid(), target).apply(u, v));
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), hillClimbing, source, target));
				app.showMessage(format("Hill Climbing (Manhattan) time: %.6f seconds.", watch.getSeconds()));
			} else if (pathFinderInfo.isTagged(PathFinderTag.HillClimbingEuclidian)) {
				HillClimbing hillClimbing = new HillClimbing(app.model.getGrid(),
						(u, v) -> euclidianValuation(app.model.getGrid(), target).apply(u, v));
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), hillClimbing, source, target));
				app.showMessage(format("Hill Climbing (Euclidian) time: %.6f seconds.", watch.getSeconds()));
			} else {
				DepthFirstTraversal2 dfs = new DepthFirstTraversal2(app.model.getGrid());
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), dfs, source, target));
				app.showMessage(format("DFS time: %.6f seconds.", watch.getSeconds()));
			}
		}
	}
}