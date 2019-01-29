package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.view.GridDisplayArea;
import de.amr.graph.grid.api.GridPosition;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.BreadthFirstTraversalAnimation;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.util.StopWatch;

public abstract class CreateMazeActionBase extends AbstractAction {

	protected final MazeDemoApp app;

	public CreateMazeActionBase(MazeDemoApp app) {
		this.app = app;
	}

	protected GridDisplayArea getCanvas() {
		return app.wndDisplayArea.getCanvas();
	}

	protected void pause(float seconds) {
		try {
			Thread.sleep(Math.round(seconds * 1000));
		} catch (InterruptedException e) {
		}
	}

	protected void floodFill(OrthogonalGrid grid) {
		BreadthFirstTraversalAnimation.floodFill(getCanvas(), grid, grid.cell(app.model.getGenerationStart()));
	}

	@SuppressWarnings("unchecked")
	private MazeGenerator<OrthogonalGrid> createGenerator(AlgorithmInfo algo) throws Exception {
		return (MazeGenerator<OrthogonalGrid>) algo.getAlgorithmClass().getConstructor(Integer.TYPE, Integer.TYPE)
				.newInstance(app.model.getGridWidth(), app.model.getGridHeight());
	}

	protected OrthogonalGrid createMaze(AlgorithmInfo algo, GridPosition startPosition)
			throws Exception, StackOverflowError {
		MazeGenerator<OrthogonalGrid> generator = createGenerator(algo);
		getCanvas().setGrid(generator.getGrid());
		getCanvas().clear();
		getCanvas().drawGrid();
		int startCell = generator.getGrid().cell(startPosition);
		int x = generator.getGrid().col(startCell), y = generator.getGrid().row(startCell);
		app.showMessage(format("\n%s (%d cells)", algo.getDescription(), generator.getGrid().numVertices()));
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
		return generator.getGrid();
	}
}
