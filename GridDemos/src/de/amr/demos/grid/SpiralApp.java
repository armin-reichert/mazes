package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.iterators.traversals.Spiral;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class SpiralApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SpiralApp());
	}

	public SpiralApp() {
		super(2, Top4.get());
		setAppName("Spiral");
	}

	@Override
	public void run() {
		canvas.setDelay(0);
		grid.vertexStream().forEach(cell -> {
			grid.set(cell, COMPLETED);
		});
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
