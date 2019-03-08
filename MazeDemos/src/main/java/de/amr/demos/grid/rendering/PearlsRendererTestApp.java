package de.amr.demos.grid.rendering;

import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.SwingGridSampleApp;
import de.amr.graph.grid.ui.animation.DFSAnimation;
import de.amr.graph.pathfinder.api.TraversalState;
import de.amr.graph.pathfinder.impl.DepthFirstSearch2;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.ust.WilsonUSTRecursiveCrosses;

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
		getGrid().setDefaultVertexLabel(v -> TraversalState.COMPLETED);
		getGrid().fill();
		getCanvas().drawGrid();

		sleep(2000);
		clear();
		setCanvasAnimation(true);
		MazeGenerator<OrthogonalGrid> generator = new WilsonUSTRecursiveCrosses(GRID_SIZE, GRID_SIZE);
		setGrid(generator.getGrid());
		generator.createMaze(0, 0);

		sleep(2000);
		DFSAnimation.builder().canvas(getCanvas()).build().run(new DepthFirstSearch2<>(getGrid()), 0,
				getGrid().cell(BOTTOM_RIGHT));

		sleep(2000);
		getCanvas().clear();
		floodfill(TOP_LEFT);
	}

	private void clear() {
		getGrid().removeEdges();
		getGrid().clearVertexLabels();
		getGrid().setDefaultVertexLabel(v -> TraversalState.UNVISITED);
		getCanvas().clear();
	}
}