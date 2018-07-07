package de.amr.demos.grid.iterators;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.impl.iterators.traversals.Spiral;

public class SpiralApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SpiralApp());
	}

	public SpiralApp() {
		super(2);
		setAppName("Spiral");
	}

	@Override
	public void run() {
		getGrid().vertices().forEach(cell -> {
			getGrid().set(cell, COMPLETED);
		});
		Spiral spiral = new Spiral(getGrid(), getGrid().cell(CENTER));
		Integer prevCell = null;
		for (Integer cell : spiral) {
			getGrid().set(cell, VISITED);
			if (prevCell != null) {
				getGrid().set(prevCell, UNVISITED);
			}
			prevCell = cell;
		}
	}
}
