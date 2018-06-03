package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.lang.String.format;

import java.awt.Color;
import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * Action for creating a maze using the selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateSingleMazeAction extends MazeDemoAction {

	public CreateSingleMazeAction(MazeDemoApp app) {
		super(app, "Create Maze");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.settingsWindow.getGeneratorMenu().getSelectedAlgorithm().ifPresent(this::createMaze);
	}

	private void createMaze(AlgorithmInfo generatorInfo) {
		app.getCanvas().fill(Color.BLACK);
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		enableUI(false);
		app.startTask(() -> {
			try {
				runMazeGenerator(generatorInfo, app.model.getGrid().cell(app.model.getGenerationStart()));
			} catch (Throwable x) {
				x.printStackTrace(System.err);
				app.showMessage("An exception occured: " + x);
				app.model.setGrid(new ObservableGrid<>(app.model.getGrid().numCols(), app.model.getGrid().numRows(), Top4.get(),
						UNVISITED, false));
				app.newCanvas();

			} finally {
				enableUI(true);
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
			}
		});
	}

	protected void runMazeGenerator(AlgorithmInfo generatorInfo, int startCell) throws Exception, StackOverflowError {
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numCells()));
		app.model.getGrid().setEventsEnabled(false);
		app.model.getGrid().clearContent();
		app.model.getGrid().setDefaultContent(UNVISITED);
		app.model.getGrid().removeEdges();
		app.model.getGrid().setEventsEnabled(true);
		MazeAlgorithm generator = (MazeAlgorithm) generatorInfo.getAlgorithmClass().getConstructor(Grid2D.class)
				.newInstance(app.model.getGrid());
		if (app.model.isGenerationAnimated()) {
			generator.run(startCell);
		} else {
			app.getCanvas().stopListening();
			watch.runAndMeasure(() -> generator.run(startCell));
			app.showMessage(format("Maze generation: %.6f seconds.", watch.getSeconds()));
			// no event handling, so we must explicitly render grid
			watch.runAndMeasure(() -> app.getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.6f seconds.", watch.getSeconds()));
			app.getCanvas().startListening();
		}
	}
}