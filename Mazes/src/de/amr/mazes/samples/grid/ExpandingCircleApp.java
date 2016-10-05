package de.amr.mazes.samples.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

public class ExpandingCircleApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new ExpandingCircleApp());
	}

	public ExpandingCircleApp() {
		super("Expanding Circle", 300, 300, 2);
	}

	@Override
	public void run() {
		setDelay(4);
		new ExpandingCircle<>(grid, grid.cell(CENTER), 0, grid.numRows()).forEach(cell -> grid.set(cell, COMPLETED));
	}
}