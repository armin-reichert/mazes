package de.amr.mazes.samples.grid;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.iterators.traversals.Spiral;

public class SpiralApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new SpiralApp());
	}

	public SpiralApp() {
		super("Spiral", 200, 150, 4);
	}

	@Override
	public void run() {
		canvas.setDelay(0);
		for (Integer cell : grid.vertexSequence()) {
			grid.set(cell, TraversalState.COMPLETED);
		}
		canvas.setDelay(4);
		Spiral<Integer> spiral = new Spiral<>(grid, grid.cell(GridPosition.CENTER));
		Integer prevCell = null;
		for (Integer cell : spiral) {
			grid.set(cell, TraversalState.VISITED);
			if (prevCell != null) {
				grid.set(prevCell, TraversalState.UNVISITED);
			}
			prevCell = cell;
		}
	}
}
