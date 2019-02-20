package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;
import static de.amr.demos.maze.swingapp.MazeDemoApp.controlWindow;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.AnimationInterruptedException;

/**
 * Action for running all maze generators (except slow ones) one by one.
 * 
 * @author Armin Reichert
 */
public class CreateAllMazesAction extends CreateMazeActionBase {

	public CreateAllMazesAction() {
		putValue(NAME, "All Mazes");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().enableUI(false);
		app().startWorkerThread(() -> {
			try {
				createAllMazes();
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
	}

	private void createAllMazes() {
		List<AlgorithmInfo> generators = Stream.of(MazeDemoModel.GENERATOR_ALGORITHMS)
				.filter(algo -> !algo.isTagged(MazeGenerationAlgorithmTag.Slow)).collect(Collectors.toList());
		for (AlgorithmInfo algo : generators) {
			canvas().clear();
			controlWindow().generatorMenu.selectAlgorithm(algo);
			app().onGeneratorChange(algo);
			OrthogonalGrid maze = null;
			try {
				maze = createMaze(algo, model().getGenerationStart());
				if (maze != null && model().isFloodFillAfterGeneration()) {
					pause(1);
					floodFill();
				}
				pause(3);
			} catch (AnimationInterruptedException x) {
				throw x;
			} catch (StackOverflowError | Exception x) {
				app().showMessage("Error during generation: " + x.getClass().getSimpleName());
				app().resetDisplay();
				x.printStackTrace();
			}
			pause(1);
		}
		app().showMessage("Done.");
	}
}