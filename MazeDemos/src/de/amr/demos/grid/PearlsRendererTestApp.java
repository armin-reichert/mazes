package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
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
		getGrid().setDefaultVertexLabel(COMPLETED);
		getGrid().fill();
		getCanvas().drawGrid();

		sleep(2000);
		clear();
		setCanvasAnimation(true);
		OrthogonalMazeGenerator generator = new WilsonUSTRecursiveCrosses(GRID_SIZE, GRID_SIZE);
		setGrid(generator.getGrid());
		generator.createMaze(0, 0);

		sleep(2000);
		new DepthFirstTraversalAnimation(getGrid()).run(getCanvas(), new DepthFirstTraversal2(getGrid()), 0,
				getGrid().cell(BOTTOM_RIGHT));

		sleep(2000);
		getCanvas().clear();
		floodFill(getCanvas(), getGrid(), getGrid().cell(TOP_LEFT), true);
	}

	private void clear() {
		getGrid().removeEdges();
		getGrid().clearVertexLabels();
		getGrid().setDefaultVertexLabel(UNVISITED);
		getCanvas().clear();
	}
}