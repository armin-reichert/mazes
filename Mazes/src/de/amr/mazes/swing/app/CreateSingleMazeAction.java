package de.amr.mazes.swing.app;

import static java.lang.String.format;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.maze.misc.StopWatch;
import de.amr.mazes.swing.model.AlgorithmInfo;
import de.amr.mazes.swing.rendering.BFSAnimation;
import de.amr.mazes.swing.rendering.DFSAnimation;
import de.amr.mazes.swing.view.ControlPanel;

/**
 * Action for creating a maze and optionally running a path finder.
 */
public class CreateSingleMazeAction extends AbstractAction {

	protected final MazeDemoApp app;

	public CreateSingleMazeAction(MazeDemoApp app) {
		super("Create Maze");
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.canvas().setDelay(app.model.getDelay());
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
			} catch (Exception x) {
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
		// Prepare grid
		app.grid().setEventsEnabled(false);
		app.grid().clear();
		app.grid().setDefault(TraversalState.UNVISITED);
		app.grid().removeEdges();
		app.grid().setEventsEnabled(true);

		app.showMessage(String.format("%d cells, %s", app.grid().numCells(), generatorInfo.getDescription()));

		// Create generator object (must happen after grid preparation)
		@SuppressWarnings("unchecked")
		Consumer<Integer> generator = (Consumer<Integer>) generatorInfo.getAlgorithmClass()
				.getConstructor(ObservableDataGrid2D.class).newInstance(app.model.getGrid());

		app.canvas().resetRenderingModel();
		if (app.grid().edgeCount() > 0) {
			app.canvas().render();
		}

		Integer startCell = app.grid().cell(app.model.getGenerationStart());
		if (app.model.isGenerationAnimated()) {
			generator.accept(startCell);
		} else {
			// Silent generation without animation
			app.canvas().stopListening();
			StopWatch watch = new StopWatch();
			watch.measure(() -> generator.accept(startCell));
			app.showMessage(format("Done in %.6f seconds.", watch.getDuration()));
			// Render maze
			app.canvas().clear();
			app.canvas().render();
			app.canvas().startListening();
		}
	}

	protected void runPathFinder(AlgorithmInfo<?> pathFinder) {
		final Integer source = app.grid().cell(app.model.getPathFinderSource());
		final Integer target = app.grid().cell(app.model.getPathFinderTarget());

		if (pathFinder.getAlgorithmClass() == BFSAnimation.class) {
			final BFSAnimation bfs = new BFSAnimation(app.canvas(), app.grid());
			bfs.runAnimation(source);
			if (app.model.isLongestPathHighlighted()) {
				bfs.showPath(bfs.getMaxDistanceCell());
			} else {
				bfs.showPath(app.grid().cell(app.model.getPathFinderTarget()));
			}
		} else if (pathFinder.getAlgorithmClass() == DFSAnimation.class) {
			new DFSAnimation(app.canvas(), app.grid(), source, target).runAnimation();
		}
	}
}
