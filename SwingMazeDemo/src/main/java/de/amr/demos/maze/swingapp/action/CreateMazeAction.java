package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * Action for creating a maze using the currently selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateMazeAction extends CreateMazeActionBase {

	public CreateMazeAction(MazeDemoApp app) {
		super(app);
		putValue(NAME, "New Maze");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.generatorMenu.getSelectedAlgorithm().ifPresent(algo -> {
			app.enableUI(false);
			app.startWorkerThread(() -> {
				try {
					OrthogonalGrid maze = createMaze(algo, app.model.getGenerationStart());
					if (maze != null && app.model.isFloodFillAfterGeneration()) {
						pause(1);
						floodFill(maze);
					}
				} catch (Exception | StackOverflowError x) {
					app.showMessage("Error during generation: " + x.getClass().getSimpleName());
					app.resetDisplay();
					x.printStackTrace(System.err);
				} finally {
					app.enableUI(true);
				}
			});
		});
	}

}