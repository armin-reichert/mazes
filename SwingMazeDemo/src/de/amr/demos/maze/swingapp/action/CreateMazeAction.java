package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.util.StopWatch;

/**
 * Action for creating a maze using the selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateMazeAction extends MazeDemoAction {

	public CreateMazeAction(MazeDemoApp app) {
		super(app, "Create");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.generatorMenu.getSelectedAlgorithm().ifPresent(this::createMaze);
	}

	private void createMaze(AlgorithmInfo generatorInfo) {
		app.enableUI(false);
		app.startWorkerThread(() -> {
			try {
				runMazeGenerator(generatorInfo, app.model.getGenerationStart());
			} catch (Exception | StackOverflowError x) {
				x.printStackTrace(System.err);
				app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
				app.newCanvas();
			} finally {
				app.enableUI(true);
			}
		});
	}

	protected void runMazeGenerator(AlgorithmInfo generatorInfo, GridPosition startPosition)
			throws Exception, StackOverflowError {
		ObservableMazeGenerator generator = (ObservableMazeGenerator) generatorInfo.getAlgorithmClass()
				.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(app.model.getGridWidth(), app.model.getGridHeight());
		app.setGrid(generator.getGrid());
		int startCell = generator.getGrid().cell(startPosition);
		int x = generator.getGrid().col(startCell), y = generator.getGrid().row(startCell);
		app.getCanvas().clear();
		app.getCanvas().drawGrid();
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numVertices()));
		if (app.model.isGenerationAnimated()) {
			generator.createMaze(x, y);
		} else {
			StopWatch watch = new StopWatch();
			app.getCanvasAnimation().setEnabled(false);
			watch.measure(() -> generator.createMaze(x, y));
			app.showMessage(format("Maze generation: %.2f seconds.", watch.getSeconds()));
			app.getCanvasAnimation().setEnabled(true);
			watch.measure(() -> app.getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.2f seconds.", watch.getSeconds()));
		}
	}
}