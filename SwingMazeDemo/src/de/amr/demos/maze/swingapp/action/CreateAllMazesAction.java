package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;

import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.easy.grid.api.GridPosition;

/**
 * Action for running a sequence of all (except very slow ones) maze generators in sequence.
 * 
 * @author Armin Reichert
 */
public class CreateAllMazesAction extends CreateMazeAction {

	private boolean ready;

	public CreateAllMazesAction(MazeDemoApp app) {
		super(app);
		putValue(NAME, "All Mazes");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.enableUI(false);
		app.startWorkerThread(() -> {
			try {
				generateAllMazes();
			} finally {
				app.enableUI(true);
			}
		});
	}

	private void generateAllMazes() {
		ready = true;
		/*@formatter:off*/
		Stream.of(MazeDemoModel.GENERATOR_ALGORITHMS)
			.filter(generatorInfo -> !generatorInfo.isTagged(Slow))
			.forEach(generatorInfo -> {
				if (app.isWorkerThreadStopped()) {
					return;
				}
				if (ready) {
					createNextMaze(generatorInfo, app.model.getGenerationStart());
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

	private void createNextMaze(AlgorithmInfo generatorInfo, GridPosition startPosition) {
		ready = false;
		app.getCanvas().clear();
		app.wndSettings.generatorMenu.selectAlgorithm(generatorInfo);
		app.onGeneratorChange(generatorInfo);
		try {
			runMazeGenerator(generatorInfo, startPosition);
		} catch (Exception | StackOverflowError x) {
			app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
			app.newCanvas();
		} finally {
			ready = true;
		}
	}
}