package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.view.GridDisplayArea;
import de.amr.easy.graph.grid.api.GridPosition;
import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.graph.grid.ui.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.core.MazeGenerator;
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
				MazeGenerator<OrthogonalGrid> generator = runMazeGenerator(generatorInfo,
						app.model.getGenerationStart());
				if (app.model.isFloodFillAfterGeneration()) {
					floodFill(generator.getGrid());
				}
			} catch (Exception | StackOverflowError x) {
				x.printStackTrace(System.err);
				app.showMessage("Maze generation aborted: " + x.getClass().getSimpleName());
				app.resetDisplay();
			} finally {
				app.enableUI(true);
			}
		});
	}

	protected void floodFill(OrthogonalGrid grid) {
		BreadthFirstTraversalAnimation.floodFill(getCanvas(), grid, grid.cell(app.model.getGenerationStart()));
	}

	protected GridDisplayArea getCanvas() {
		return app.wndDisplayArea.getCanvas();
	}

	protected MazeGenerator<OrthogonalGrid> runMazeGenerator(AlgorithmInfo generatorInfo,
			GridPosition startPosition) throws Exception, StackOverflowError {
		@SuppressWarnings("unchecked")
		MazeGenerator<OrthogonalGrid> generator = (MazeGenerator<OrthogonalGrid>) generatorInfo
				.getAlgorithmClass().getConstructor(Integer.TYPE, Integer.TYPE)
				.newInstance(app.model.getGridWidth(), app.model.getGridHeight());
		app.showMessage(
				format("\n%s (%d cells)", generatorInfo.getDescription(), generator.getGrid().numVertices()));
		getCanvas().setGrid(generator.getGrid());
		getCanvas().clear();
		getCanvas().drawGrid();
		int startCell = generator.getGrid().cell(startPosition);
		int x = generator.getGrid().col(startCell), y = generator.getGrid().row(startCell);
		if (app.model.isGenerationAnimated()) {
			generator.createMaze(x, y);
		} else {
			getCanvas().getAnimation().setEnabled(false);
			StopWatch watch = new StopWatch();
			watch.measure(() -> generator.createMaze(x, y));
			app.showMessage(format("Maze generation: %.2f seconds.", watch.getSeconds()));
			getCanvas().clear();
			watch.measure(() -> getCanvas().drawGrid());
			app.showMessage(format("Grid rendering:  %.2f seconds.", watch.getSeconds()));
			getCanvas().getAnimation().setEnabled(true);
		}
		return generator;
	}
}