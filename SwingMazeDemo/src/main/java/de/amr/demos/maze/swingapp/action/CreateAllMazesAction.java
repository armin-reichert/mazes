package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;
import static de.amr.demos.maze.swingapp.MazeDemoApp.controlWindow;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Slow;

import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.graph.grid.impl.OrthogonalGrid;

/**
 * Action for running all maze generators (except slow ones) one by one.
 * 
 * @author Armin Reichert
 */
public class CreateAllMazesAction extends CreateMazeActionBase {

	private boolean readyForNext;

	public CreateAllMazesAction() {
		putValue(NAME, "All Mazes");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().startWorkerThread(() -> {
			app().enableUI(false);
			try {
				createAllMazes();
			} finally {
				app().enableUI(true);
			}
		});
	}

	private void createAllMazes() {
		readyForNext = true;
		/*@formatter:off*/
		Stream.of(GENERATOR_ALGORITHMS)
			.filter(algo -> !algo.isTagged(Slow))
			.forEach(algo -> {
				if (readyForNext && !app().isWorkerThreadStopped()) {
					try {
						OrthogonalGrid maze = createNextMaze(algo);
						if (maze != null && model().isFloodFillAfterGeneration()) {
							pause(1);
							floodFill();
						}
						pause(3);
					} catch (Exception x) {
						x.printStackTrace();
					}
				}
		});
		/*@formatter:on*/
		app().showMessage("Done.");
	}

	private OrthogonalGrid createNextMaze(AlgorithmInfo algo) {
		canvas().clear();
		controlWindow().generatorMenu.selectAlgorithm(algo);
		app().onGeneratorChange(algo);
		try {
			readyForNext = false;
			return createMaze(algo, model().getGenerationStart());
		} catch (Exception | StackOverflowError x) {
			app().showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
			app().resetDisplay();
			return null;
		} finally {
			readyForNext = true;
		}
	}
}