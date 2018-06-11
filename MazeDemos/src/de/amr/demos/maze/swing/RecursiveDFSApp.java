package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.traversal.RecursiveDFS;

public class RecursiveDFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDFSApp());
	}

	public RecursiveDFSApp() {
		super(256, Top4.get());
		setAppName("Recursive-DFS Maze");
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32).forEach(cellSize -> {
			resizeGrid(cellSize);
			new RecursiveDFS(grid).run(grid.cell(TOP_LEFT));
			BreadthFirstTraversalAnimation.floodFill(canvas, grid, 0);
			sleep(1000);
		});
		System.exit(0);
	}
}