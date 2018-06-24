package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.min;

import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

public class ExpandingCircleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new ExpandingCircleApp());
	}

	public ExpandingCircleApp() {
		super(2);
		setAppName("Expanding Circle");
	}

	@Override
	public void run() {
		int n = min(getGrid().numCols() / 2, getGrid().numRows() / 2);
		new ExpandingCircle(getGrid(), getGrid().cell(CENTER), 0, n).forEach(cell -> getGrid().set(cell, COMPLETED));
	}
}