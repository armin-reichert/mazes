package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.Color;

import de.amr.easy.graph.traversal.DepthFirstTraversal2;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.DepthFirstTraversalAnimation;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;

public class PearlsRendererTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new PearlsRendererTestApp());
	}

	public PearlsRendererTestApp() {
		super(750, 750, 30, Top4.get());
		setAppName("Full Grid");
		setRenderingStyle(Style.PEARLS);
	}

	@Override
	public void run() {
		grid.setDefaultContent(COMPLETED);
		canvas.stopListening();
		grid.fill();
		canvas.drawGrid();
		sleep(2000);
		clear();
		canvas.startListening();
		canvas.setDelay(20);
		new WilsonUSTRecursiveCrosses(grid).run(0);
		canvas.setDelay(10);
		new DepthFirstTraversalAnimation<>(grid).run(canvas, new DepthFirstTraversal2<ObservableGrid<?, ?>>(grid), 0,
				grid.cell(GridPosition.BOTTOM_RIGHT));
		floodFill(canvas, grid, 0);
	}

	private void clear() {
		grid.removeEdges();
		grid.clearContent();
		grid.setDefaultContent(UNVISITED);
		canvas.fill(Color.BLACK);
	}
}