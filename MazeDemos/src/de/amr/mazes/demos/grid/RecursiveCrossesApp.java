package de.amr.mazes.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.grid.iterators.traversals.RecursiveCrosses;

public class RecursiveCrossesApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveCrossesApp());
	}

	public RecursiveCrossesApp() {
		super("Recursive Crosses", 60, 30, 16);
	}

	@Override
	public void run() {
		setDelay(6);
		new RecursiveCrosses(grid).forEach(cell -> grid.set(cell, COMPLETED));
	}
}