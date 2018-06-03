package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.SmallGrid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;

/**
 * Action for running a sequence of all (without restricted/slow ones) available maze generation
 * algorithms in sequence.
 * 
 * @author Armin Reichert
 */
public class CreateAllMazesAction extends CreateSingleMazeAction {

	private boolean readyForNext;

	public CreateAllMazesAction(MazeDemoApp app) {
		super(app);
		putValue(NAME, "Create All Mazes");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (app.model.isHidingControlsWhenRunning()) {
			app.settingsWindow.setVisible(false);
		}
		app.getCanvas().setDelay(app.model.getDelay());
		app.mazeWindow.setVisible(true);
		app.startTask(() -> {
			enableUI(false);
			try {
				generateAllMazes();
			} finally {
				enableUI(true);
				app.settingsWindow.setVisible(true);
			}
		});
	}

	private void generateAllMazes() {
		readyForNext = true;
		/*@formatter:off*/
		Stream.of(MazeDemoModel.GENERATOR_ALGORITHMS)
			.filter(alg -> !(alg.isTagged(Slow) || alg.isTagged(SmallGrid)))
			.forEachOrdered(alg -> {
				if (app.isTaskStopped()) {
					return;
				}
				if (readyForNext) {
					createNextMaze(alg);
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

	private void createNextMaze(AlgorithmInfo algorithm) {
		readyForNext = false;
		app.getCanvas().fill(Color.BLACK);
		app.settingsWindow.getControlPanel().getLblGenerationAlgorithm().setText(algorithm.getDescription());
		app.settingsWindow.getGeneratorMenu().setSelectedAlgorithm(algorithm);
		try {
			runMazeGenerator(algorithm, app.model.getGrid().cell(app.model.getGenerationStart()));
		} catch (Throwable x) {
			x.printStackTrace();
		} finally {
			readyForNext = true;
		}
	}
}