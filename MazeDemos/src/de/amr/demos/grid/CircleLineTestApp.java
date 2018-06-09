package de.amr.demos.grid;

import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.Color;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.maze.alg.ust.WilsonUSTHilbertCurve;

public class CircleLineTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new CircleLineTestApp());
	}

	public CircleLineTestApp() {
		super(800, 800, 30, Top4.get());
		setAppName("Full Grid");
		setRenderingStyle(ConfigurableGridRenderer.Style.CircleLine);
	}

	@Override
	public void run() {
		grid.setDefaultContent(TraversalState.COMPLETED);
		canvas.stopListening();
		grid.fill();
		canvas.drawGrid();
		sleep(2000);
		clear();
		canvas.startListening();
		canvas.setDelay(10);
		new WilsonUSTHilbertCurve(grid).run(0);
		floodFill(canvas, grid, 0);
	}

	private void clear() {
		grid.removeEdges();
		grid.clearContent();
		grid.setDefaultContent(TraversalState.UNVISITED);
		canvas.fill(Color.BLACK);
	}
}
