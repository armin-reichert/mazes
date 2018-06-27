package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.util.StopWatch;

/**
 * Action for creating a maze using the selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateMazeAction extends AbstractAction {

	protected final MazeDemoApp app;

	public CreateMazeAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "New Maze");
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
				app.resetDisplay();
			} finally {
				app.enableUI(true);
			}
		});
	}

	protected void runMazeGenerator(AlgorithmInfo generatorInfo, GridPosition startPosition)
			throws Exception, StackOverflowError {
		OrthogonalMazeGenerator generator = (OrthogonalMazeGenerator) generatorInfo.getAlgorithmClass()
				.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(app.model.getGridWidth(), app.model.getGridHeight());
		int startCell = generator.getGrid().cell(startPosition);
		int x = generator.getGrid().col(startCell), y = generator.getGrid().row(startCell);
		app.wndDisplayArea.getCanvas().setGrid(generator.getGrid());
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numVertices()));
		app.wndDisplayArea.getCanvas().clear();
		app.wndDisplayArea.getCanvas().drawGrid();
		if (app.model.isGenerationAnimated()) {
			generator.createMaze(x, y);
		} else {
			app.wndDisplayArea.getCanvas().getAnimation().setEnabled(false);
			StopWatch watch = new StopWatch();
			watch.measure(() -> generator.createMaze(x, y));
			app.showMessage(format("Maze generation: %.2f seconds.", watch.getSeconds()));
			app.wndDisplayArea.getCanvas().clear();
			watch.measure(() -> app.wndDisplayArea.getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.2f seconds.", watch.getSeconds()));
			app.wndDisplayArea.getCanvas().getAnimation().setEnabled(true);
		}
	}
}