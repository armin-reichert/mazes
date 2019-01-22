package de.amr.demos.grid.rendering;

import static de.amr.easy.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.graph.grid.ui.animation.BreadthFirstTraversalAnimation.floodFill;
import static de.amr.easy.graph.pathfinder.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.pathfinder.api.TraversalState.UNVISITED;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.graph.grid.impl.OrthogonalGrid;
import de.amr.easy.graph.grid.ui.animation.DepthFirstTraversalAnimation;
import de.amr.easy.graph.pathfinder.impl.DepthFirstSearchPathFinder2;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;

public class PearlsRendererTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new PearlsRendererTestApp());
	}

	static final int GRID_SIZE = 10;
	static final int CANVAS_SIZE = 800;
	static final int GRID_CELL_SIZE = CANVAS_SIZE / GRID_SIZE;

	public PearlsRendererTestApp() {
		super(CANVAS_SIZE, CANVAS_SIZE, GRID_CELL_SIZE);
		setAppName("Pearls Renderer Test");
		setStyle(Style.PEARLS);
	}

	@Override
	public void run() {
		clear();
		setCanvasAnimation(false);
		getGrid().setDefaultVertexLabel(v -> COMPLETED);
		getGrid().fill();
		getCanvas().drawGrid();

		sleep(2000);
		clear();
		setCanvasAnimation(true);
		MazeGenerator<OrthogonalGrid> generator = new WilsonUSTRecursiveCrosses(GRID_SIZE, GRID_SIZE);
		setGrid(generator.getGrid());
		generator.createMaze(0, 0);

		sleep(2000);
		new DepthFirstTraversalAnimation(getGrid()).run(getCanvas(),
				new DepthFirstSearchPathFinder2(getGrid()), 0, getGrid().cell(BOTTOM_RIGHT));

		sleep(2000);
		getCanvas().clear();
		floodFill(getCanvas(), getGrid(), getGrid().cell(TOP_LEFT), true);
	}

	private void clear() {
		getGrid().removeEdges();
		getGrid().clearVertexLabels();
		getGrid().setDefaultVertexLabel(v -> UNVISITED);
		getCanvas().clear();
	}
}