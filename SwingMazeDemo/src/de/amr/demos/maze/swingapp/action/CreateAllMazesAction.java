package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.SmallGrid;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;

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
			enableControls(false);
			try {
				generateAllMazes();
			} finally {
				enableControls(true);
				app.settingsWindow.setVisible(true);
			}
		});
	}

	private void generateAllMazes() {
		readyForNext = true;
		/*@formatter:off*/
		Stream.of(MazeDemoModel.ALGORITHMS)
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
		app.settingsWindow.getAlgorithmMenu().setSelectedAlgorithm(algorithm);
		try {
			generateMaze(algorithm);
			app.settingsWindow.getPathFinderMenu().getSelectedAlgorithm().ifPresent(this::runPathFinder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		readyForNext = true;
	}
}