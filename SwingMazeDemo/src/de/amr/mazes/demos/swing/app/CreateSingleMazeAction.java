package de.amr.mazes.demos.swing.app;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.lang.String.format;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.grid.rendering.swing.DFSAnimation;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.util.StopWatch;
import de.amr.mazes.demos.swing.model.AlgorithmInfo;
import de.amr.mazes.demos.swing.view.ControlPanel;

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
		app.canvas().resetRenderingModel();
		app.canvas().clear();

		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);

		app.startTask(() -> {
			try {
				enableControls(false);
				generateMaze(app.settingsWindow.getAlgorithmMenu().getSelectedAlgorithm());
				AlgorithmInfo<?> pathFinder = app.settingsWindow.getPathFinderMenu().getSelectedPathFinder();
				if (pathFinder != null) {
					runPathFinder(pathFinder);
				}
			} catch (Throwable x) {
				x.printStackTrace(System.err);
				app.showMessage("An exception occured: " + x);
			} finally {
				enableControls(true);
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
			}
		});
	}

	protected void enableControls(boolean b) {
		ControlPanel controls = app.settingsWindow.getControlPanel();
		controls.getBtnCreateMaze().setEnabled(b);
		controls.getBtnCreateAllMazes().setEnabled(b);
		controls.getPassageThicknessSlider().setEnabled(b);
		controls.getResolutionSelector().setEnabled(b);
	}

	protected void generateMaze(AlgorithmInfo<?> generatorInfo) throws Exception {
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.grid().numCells()));

		// Reset grid
		app.grid().setEventsEnabled(false);
		app.grid().clearContent();
		app.grid().setDefaultContent(UNVISITED);
		app.grid().removeEdges();
		app.grid().setEventsEnabled(true);

		app.canvas().clear();

		// Create generator instance
		MazeAlgorithm generator = (MazeAlgorithm) generatorInfo.getAlgorithmClass().getConstructor(Grid2D.class)
				.newInstance(app.model.getGrid());

		// Create maze
		Integer startCell = app.grid().cell(app.model.getGenerationStart());
		if (app.model.isGenerationAnimated()) {
			// event handlers do the rendering
			generator.accept(startCell);
		} else {
			// no animation, must render explicitly
			app.canvas().stopListening();
			watch.runAndMeasure(() -> generator.accept(startCell));
			app.showMessage(format("Generation time: %.6f seconds.", watch.getSeconds()));
			watch.runAndMeasure(() -> app.canvas().render());
			app.showMessage(format("Rendering time:  %.6f seconds.", watch.getSeconds()));
			app.canvas().startListening();
		}
	}

	protected void runPathFinder(AlgorithmInfo<?> pathFinder) {
		final Integer source = app.grid().cell(app.model.getPathFinderSource());
		final Integer target = app.grid().cell(app.model.getPathFinderTarget());
		if (pathFinder.getAlgorithmClass() == BFSAnimation.class) {
			final BFSAnimation bfs = new BFSAnimation(app.canvas(), app.grid());
			watch.runAndMeasure(() -> bfs.runAnimation(source));
			app.showMessage(format("BFS time: %.6f seconds.", watch.getSeconds()));

			if (app.model.isLongestPathHighlighted()) {
				bfs.showPath(bfs.getMaxDistanceCell());
			} else {
				bfs.showPath(app.grid().cell(app.model.getPathFinderTarget()));
			}
		} else if (pathFinder.getAlgorithmClass() == DFSAnimation.class) {
			watch.runAndMeasure(() -> new DFSAnimation(app.canvas(), app.grid(), source, target).runAnimation());
			app.showMessage(format("DFS time: %.6f seconds.", watch.getSeconds()));
		}
	}
}
