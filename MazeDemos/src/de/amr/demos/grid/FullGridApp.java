package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.awt.EventQueue;

public class FullGridApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new FullGridApp());
	}

	public FullGridApp() {
		super("Full Grid", 2);
		grid.setDefaultContent(COMPLETED);
		grid.makeFullGrid();
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
