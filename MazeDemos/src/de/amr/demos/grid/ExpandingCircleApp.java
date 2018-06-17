package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static java.lang.Math.min;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.iterators.traversals.ExpandingCircle;

public class ExpandingCircleApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new ExpandingCircleApp());
	}

	public ExpandingCircleApp() {
		super(2, Top4.get(), SimpleEdge::new);
		setAppName("Expanding Circle");
	}

	@Override
	public void run() {
		int n = min(grid.numCols() / 2, grid.numRows() / 2);
		new ExpandingCircle(grid, grid.cell(CENTER), 0, n).forEach(cell -> grid.set(cell, COMPLETED));
	}
}