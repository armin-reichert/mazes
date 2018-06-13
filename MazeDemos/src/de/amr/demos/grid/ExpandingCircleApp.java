package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.min;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

public class ExpandingCircleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new ExpandingCircleApp());
	}

	public ExpandingCircleApp() {
		super(2, Top4.get());
		setAppName("Expanding Circle");
	}

	@Override
	public void run() {
		int n = min(grid.numCols(), grid.numRows());
		new ExpandingCircle(grid, grid.cell(CENTER), 0, n).forEach(cell -> grid.set(cell, COMPLETED));
	}
}