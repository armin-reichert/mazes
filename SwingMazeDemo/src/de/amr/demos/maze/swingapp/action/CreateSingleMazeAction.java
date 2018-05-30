package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.util.GridUtils.euclidianDistance;
import static de.amr.easy.util.GridUtils.manhattanDistance;
import static java.lang.String.format;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.AlgorithmTag;
import de.amr.demos.maze.swingapp.view.ControlPanel;
import de.amr.easy.graph.traversal.DepthFirstTraversal;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingDFSAnimation;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.util.StopWatch;

/**
 * Action for creating a maze and optionally running a path finder.
 */
public class CreateSingleMazeAction extends AbstractAction {

	protected final MazeDemoApp app;
	protected final StopWatch watch = new StopWatch();

	public CreateSingleMazeAction(MazeDemoApp app) {
		super("Create Maze");
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.getCanvas().fill(Color.BLACK);
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		enableControls(false);
		Optional<AlgorithmInfo> generator = app.settingsWindow.getAlgorithmMenu().getSelectedAlgorithm();
		app.startTask(() -> {
			if (generator.isPresent()) {
				try {
					generateMaze(generator.get());
					app.settingsWindow.getPathFinderMenu().getSelectedAlgorithm().ifPresent(this::runPathFinder);
				} catch (Throwable x) {
					x.printStackTrace(System.err);
					app.showMessage("An exception occured: " + x);
					app.model.setGrid(new ObservableGrid<>(app.model.getGrid().numCols(), app.model.getGrid().numRows(),
							Top4.get(), UNVISITED, false));
					app.newCanvas();

				} finally {
					enableControls(true);
					app.settingsWindow.setVisible(true);
					app.settingsWindow.requestFocus();
				}
			}
		});
	}

	protected void enableControls(boolean b) {
		ControlPanel controls = app.settingsWindow.getControlPanel();
		controls.getBtnCreateMaze().setEnabled(b);
		controls.getBtnCreateAllMazes().setEnabled(b);
		controls.getSliderPassageWidth().setEnabled(b);
		controls.getComboGridResolution().setEnabled(b);
	}

	protected void generateMaze(AlgorithmInfo generatorInfo) throws Exception {
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numCells()));

		// Reset grid
		app.model.getGrid().setEventsEnabled(false);
		app.model.getGrid().clearContent();
		app.model.getGrid().setDefaultContent(UNVISITED);
		app.model.getGrid().removeEdges();
		app.model.getGrid().setEventsEnabled(true);

		app.getCanvas().fill(Color.BLACK);

		// Create generator instance
		MazeAlgorithm generator = (MazeAlgorithm) generatorInfo.getAlgorithmClass().getConstructor(Grid2D.class)
				.newInstance(app.model.getGrid());

		// Create maze
		int startCell = app.model.getGrid().cell(app.model.getGenerationStart());
		if (app.model.isGenerationAnimated()) {
			// event handlers do the rendering
			generator.run(startCell);
		} else {
			// no animation, must render explicitly
			app.getCanvas().stopListening();
			watch.runAndMeasure(() -> generator.run(startCell));
			app.showMessage(format("Generation time: %.6f seconds.", watch.getSeconds()));
			watch.runAndMeasure(() -> app.getCanvas().drawGrid());
			app.showMessage(format("Rendering time:  %.6f seconds.", watch.getSeconds()));
			app.getCanvas().startListening();
		}
	}

	protected void runPathFinder(AlgorithmInfo pathFinderInfo) {
		int source = app.model.getGrid().cell(app.model.getPathFinderSource());
		int target = app.model.getGrid().cell(app.model.getPathFinderTarget());
		if (pathFinderInfo.getAlgorithmClass() == SwingBFSAnimation.class) {
			SwingBFSAnimation bfsAnimation = new SwingBFSAnimation(app.model.getGrid());
			bfsAnimation.setPathColor(app.model.getPathColor());
			watch.runAndMeasure(() -> bfsAnimation.runBFSAnimation(app.getCanvas(), source));
			app.showMessage(format("BFS time: %.6f seconds.", watch.getSeconds()));
			if (app.model.isLongestPathHighlighted()) {
				bfsAnimation.showPath(app.getCanvas(), bfsAnimation.getMaxDistanceCell());
			} else {
				bfsAnimation.showPath(app.getCanvas(), target);
			}
		} else if (pathFinderInfo.getAlgorithmClass() == SwingDFSAnimation.class) {
			SwingDFSAnimation dfsAnimation = new SwingDFSAnimation(app.model.getGrid());
			dfsAnimation.setPathColor(app.model.getPathColor());
			if (pathFinderInfo.isTagged(AlgorithmTag.HillClimbingManhattan)) {
				HillClimbing hillClimbing = new HillClimbing(app.model.getGrid(), source, target);
				hillClimbing.vertexValuation = (u, v) -> manhattanDistance(app.model.getGrid(), target).apply(u, v);
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), hillClimbing, source, target));
				app.showMessage(format("Hill Climbing (Manhattan) time: %.6f seconds.", watch.getSeconds()));
			} else if (pathFinderInfo.isTagged(AlgorithmTag.HillClimbingEuclidian)) {
				HillClimbing hillClimbing = new HillClimbing(app.model.getGrid(), source, target);
				hillClimbing.vertexValuation = (u, v) -> euclidianDistance(app.model.getGrid(), target).apply(u, v);
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), hillClimbing, source, target));
				app.showMessage(format("Hill Climbing (Euclidian) time: %.6f seconds.", watch.getSeconds()));
			} else {
				DepthFirstTraversal dfs = new DepthFirstTraversal(app.model.getGrid(), source, target);
				watch.runAndMeasure(() -> dfsAnimation.run(app.getCanvas(), dfs, source, target));
				app.showMessage(format("DFS time: %.6f seconds.", watch.getSeconds()));
			}
		}
	}
}
