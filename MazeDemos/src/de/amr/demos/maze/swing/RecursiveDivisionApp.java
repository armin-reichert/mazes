package de.amr.demos.maze.swing;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.RecursiveDivision;

public class RecursiveDivisionApp extends SwingGridSampleApp<Void> {

	public static void main(String[] args) {
		launch(new RecursiveDivisionApp());
	}

	public RecursiveDivisionApp() {
		super(128, Top4.get(), SimpleEdge::new);
		setAppName("Recursive Division Maze");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			grid.fill();
			grid.setDefaultVertex(COMPLETED);
			new RecursiveDivision(grid).run(grid.cell(TOP_LEFT));
			sleep(1000);
			BreadthFirstTraversalAnimation.floodFill(canvas, grid, 0);
			sleep(1000);
		});
		System.exit(0);
	}
}