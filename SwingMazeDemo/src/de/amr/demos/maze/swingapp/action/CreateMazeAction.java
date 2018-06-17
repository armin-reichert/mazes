package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;
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
		app.wndSettings.getGeneratorMenu().getSelectedAlgorithm().ifPresent(this::createMaze);
	}

	private void createMaze(AlgorithmInfo generatorInfo) {
		app.enableUI(false);
		app.getCanvas().clear();
		app.startWorkerThread(() -> {
			try {
				runMazeGenerator(generatorInfo, app.model.getGrid().cell(app.model.getGenerationStart()));
			} catch (Exception | StackOverflowError x) {
				x.printStackTrace(System.err);
				app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
				app.newCanvas();
			} finally {
				app.enableUI(true);
			}
		});
	}

	protected void runMazeGenerator(AlgorithmInfo generatorInfo, int startCell) throws Exception, StackOverflowError {
		app.showMessage(format("\n%s (%d cells)", generatorInfo.getDescription(), app.model.getGrid().numVertices()));
		app.model.getGrid().clearVertexObjects();
		app.model.getGrid().setDefaultVertex(UNVISITED);
		app.model.getGrid().setEventsEnabled(false);
		app.model.getGrid().removeEdges();
		app.model.getGrid().setEventsEnabled(true);
		MazeAlgorithm<SimpleEdge> generator = (MazeAlgorithm<SimpleEdge>) generatorInfo.getAlgorithmClass()
				.getConstructor(Grid2D.class).newInstance(app.model.getGrid());
		if (app.model.isGenerationAnimated()) {
			generator.run(startCell);
		} else {
			StopWatch watch = new StopWatch();
			app.getCanvasAnimation().setEnabled(false);
			watch.measure(() -> generator.run(startCell));
			app.showMessage(format("Maze generation: %.2f seconds.", watch.getSeconds()));
			app.getCanvasAnimation().setEnabled(true);
			watch.measure(() -> app.getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.2f seconds.", watch.getSeconds()));
		}
	}
}