package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;

/**
 * Action for running a sequence of all (except very slow ones) maze generators in sequence.
 * 
 * @author Armin Reichert
 */
public class CreateAllMazesAction extends CreateMazeAction {

	private boolean ready;

	public CreateAllMazesAction(MazeDemoApp app) {
		super(app);
		putValue(NAME, "Create All Mazes");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(!app.model.isHidingControlsWhenRunning());
		app.wndMaze.setVisible(true);
		app.getCanvas().clear();
		app.startTask(() -> {
			enableUI(false);
			try {
				generateAllMazes();
			} finally {
				enableUI(true);
				app.wndSettings.setVisible(true);
			}
		});
	}

	private void generateAllMazes() {
		ready = true;
		int startCell = app.model.getGrid().cell(app.model.getGenerationStart());
		/*@formatter:off*/
		Stream.of(MazeDemoModel.GENERATOR_ALGORITHMS)
			.filter(generatorInfo -> !generatorInfo.isTagged(Slow))
			.forEach(generatorInfo -> {
				if (app.isTaskStopped()) {
					return;
				}
				if (ready) {
					createNextMaze(generatorInfo, startCell);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// ignore
					}
				}
		});
		/*@formatter:on*/
		app.showMessage("Done.");
	}

	private void createNextMaze(AlgorithmInfo generatorInfo, int startCell) {
		ready = false;
		app.getCanvas().clear();
		app.wndSettings.getControlPanel().getLblGenerationAlgorithm().setText(generatorInfo.getDescription());
		app.wndSettings.getGeneratorMenu().setSelectedAlgorithm(generatorInfo);
		try {
			runMazeGenerator(generatorInfo, startCell);
		} catch (Exception | StackOverflowError x) {
			app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
			app.newGrid();
			app.newCanvas();
		} finally {
			ready = true;
		}
	}
}