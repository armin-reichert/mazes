package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.traversals.ExpandingRectangle;

public class RectangleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RectangleApp());
	}

	public RectangleApp() {
		super(800, 800, 2);
		setAppName("Rectangles");
	}

	@Override
	public void run() {
		Rectangle startRectangle = new Rectangle(getGrid(), getGrid().cell(0, 0), 1, 1);
		ExpandingRectangle expRect = new ExpandingRectangle(startRectangle);
		expRect.setExpandHorizontally(true);
		expRect.setExpandVertically(true);
		expRect.setExpansionRate(1);
		expRect.setMaxExpansion(getGrid().numCols());
		for (Integer cell : expRect) {
			getGrid().set(cell, COMPLETED);
		}
	}
}
