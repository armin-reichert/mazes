package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

public class IterativeDFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new IterativeDFSApp());
	}

	public IterativeDFSApp() {
		super(128, Top4.get());
		setAppName("Randomized-DFS Maze");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new IterativeDFS(grid).run(grid.cell(TOP_LEFT));
			BreadthFirstTraversalAnimation.floodFill(canvas, grid, 0);
			sleep(1000);
		});
		System.exit(0);
	}
}