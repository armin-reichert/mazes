package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.Color;

import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;

public class PearlsRendererTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new PearlsRendererTestApp());
	}

	public PearlsRendererTestApp() {
		super(750, 750, 15, Top4.get());
		setAppName("Pearls Renderer Test");
		setRenderingStyle(Style.PEARLS);
		renderer.fnGridBgColor = () -> Color.DARK_GRAY;
	}

	@Override
	public void run() {
		clear();
		canvasAnimation.setEnabled(false);
		grid.setDefaultContent(COMPLETED);
		grid.fill();
		canvas.drawGrid();

		sleep(2000);
		clear();
		canvasAnimation.setEnabled(true);
		canvasAnimation.setDelay(1);
		new WilsonUSTRecursiveCrosses(grid).run(0);
		
		sleep(2000);
		canvasAnimation.setDelay(10);
		new DepthFirstTraversalAnimation<>(grid).run(canvas, new DepthFirstTraversal2<ObservableGrid<?, ?>>(grid), 0,
				grid.cell(GridPosition.BOTTOM_RIGHT));

		sleep(2000);
		clear();
		floodFill(canvas, grid, 0);
	}

	private void clear() {
		grid.removeEdges();
		grid.clearContent();
		grid.setDefaultContent(UNVISITED);
		canvas.clear();
	}
}