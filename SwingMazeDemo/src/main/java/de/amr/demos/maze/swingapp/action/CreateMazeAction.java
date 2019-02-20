package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.controlWindow;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;

import java.awt.event.ActionEvent;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.AnimationInterruptedException;

/**
 * Action for creating a maze using the currently selected generation algorithm.
 * 
 * @author Armin Reichert
 */
public class CreateMazeAction extends CreateMazeActionBase {

	public CreateMazeAction() {
		putValue(NAME, "New Maze");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controlWindow().generatorMenu.getSelectedAlgorithm().ifPresent(algo -> {
			app().enableUI(false);
			app().startWorkerThread(() -> {
				try {
					OrthogonalGrid maze = createMaze(algo, model().getGenerationStart());
					if (maze != null && model().isFloodFillAfterGeneration()) {
						pause(1);
						floodFill();
					}
				} catch (AnimationInterruptedException x) {
					app().showMessage("Animation interrupted");
					app().resetDisplay();
				} catch (Exception | StackOverflowError x) {
					app().showMessage("Error during generation: " + x.getClass().getSimpleName());
					app().resetDisplay();
					x.printStackTrace(System.err);
				} finally {
					app().enableUI(true);
				}
			});
		});
	}
}