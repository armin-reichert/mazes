package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.Color;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.impl.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;

public class PearlsRendererTestApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new PearlsRendererTestApp());
	}

	static final int GRID_SIZE = 10;
	static final int CANVAS_SIZE = 800;
	static final int GRID_CELL_SIZE = CANVAS_SIZE / GRID_SIZE;

	public PearlsRendererTestApp() {
		super(CANVAS_SIZE, CANVAS_SIZE, GRID_CELL_SIZE, Top4.get(), SimpleEdge::new);
		setAppName("Pearls Renderer Test");
		setRenderingStyle(Style.PEARLS);
		ConfigurableGridRenderer renderer = (ConfigurableGridRenderer) canvas.getRenderer().get();
		renderer.fnGridBgColor = () -> Color.DARK_GRAY;
	}

	@Override
	public void run() {
		clear();
		canvasAnimation.setEnabled(false);
		grid.setDefaultVertex(COMPLETED);
		grid.fill();
		canvas.drawGrid();

		sleep(5000);
		clear();
		canvasAnimation.setEnabled(true);
		canvasAnimation.fnDelay = () -> 1;
		new WilsonUSTRecursiveCrosses(grid).run(0);

		sleep(5000);
		canvasAnimation.fnDelay = () -> 10;
		new DepthFirstTraversalAnimation(grid).run(canvas, new DepthFirstTraversal2(grid), 0, grid.cell(BOTTOM_RIGHT));

		sleep(5000);
		canvas.clear();
		floodFill(canvas, grid, grid.cell(TOP_LEFT), true);
	}

	private void clear() {
		grid.removeEdges();
		grid.clearVertexObjects();
		grid.setDefaultVertex(UNVISITED);
		canvas.clear();
	}
}