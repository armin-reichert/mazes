package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.grid.iterators.traversals.Spiral;

public class SpiralApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new SpiralApp());
	}

	public SpiralApp() {
		super("Spiral", 2);
	}

	@Override
	public void run() {
		canvas.setDelay(0);
		grid.vertexStream().forEach(cell -> {
			grid.set(cell, COMPLETED);
		});
		canvas.setDelay(0);
		Spiral spiral = new Spiral(grid, grid.cell(CENTER));
		Integer prevCell = null;
		for (Integer cell : spiral) {
			grid.set(cell, VISITED);
			if (prevCell != null) {
				grid.set(prevCell, UNVISITED);
			}
			prevCell = cell;
		}
	}
}
