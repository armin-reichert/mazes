package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.lang.String.format;

import java.awt.Color;
import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.MazeAlgorithm;

/**
 * Action for creating a maze using the selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateMazeAction extends MazeDemoAction {

	public CreateMazeAction(MazeDemoApp app) {
		super(app, "Create Maze");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.settingsWindow.getGeneratorMenu().getSelectedAlgorithm().ifPresent(this::createMaze);
	}

	private void createMaze(AlgorithmInfo generatorInfo) {
		enableUI(false);
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		app.getCanvas().fill(Color.BLACK);
		app.startTask(() -> {
			try {
				runMazeGenerator(generatorInfo, app.model.getGrid().cell(app.model.getGenerationStart()));
			} catch (Exception | StackOverflowError x) {
				x.printStackTrace(System.err);
				app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
				app.model.setGrid(app.newGrid());
				app.newCanvas();
			} finally {
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
				enableUI(true);
			}
		});
	}

	protected void runMazeGenerator(AlgorithmInfo generatorInfo, int startCell) throws Exception, StackOverflowError {
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numCells()));
		app.model.getGrid().clearContent();
		app.model.getGrid().setDefaultContent(UNVISITED);
		app.model.getGrid().setEventsEnabled(false);
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
			app.getCanvas().startListening();
			watch.runAndMeasure(() -> app.getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.6f seconds.", watch.getSeconds()));
		}
	}
}