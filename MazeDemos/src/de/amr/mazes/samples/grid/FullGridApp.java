package de.amr.mazes.samples.grid;

import java.awt.Dimension;
import java.awt.EventQueue;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.maze.misc.Utils;

public class FullGridApp extends GridSampleApp {

	public static void main(String[] args) {
		Dimension dim = Utils.maxGridDimensionForDisplay(2);
		launch(new FullGridApp(dim.width, dim.height, 2));
	}

	public FullGridApp(int gridWidth, int gridHeight, int cellSize) {
		super("Full Grid", gridWidth, gridHeight, cellSize);
		grid.setDefault(TraversalState.COMPLETED);
		grid.fillAllEdges();
	}

	@Override
	public void run() {
		setDelay(0);
		EventQueue.invokeLater(() -> {
			long start = System.nanoTime();
			canvas.render();
			long duration = (System.nanoTime() - start) / 1000000L;
			System.err.println("Grid rendering took " + duration + " milliseconds");
		});
	}
}
