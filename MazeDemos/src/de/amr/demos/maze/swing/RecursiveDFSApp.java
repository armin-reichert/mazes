package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.RecursiveDFS;

public class RecursiveDFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDFSApp());
	}

	public RecursiveDFSApp() {
		super("Randomized-DFS Maze");
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32).forEach(cellSize -> {
			resizeGrid(cellSize);
			new RecursiveDFS(grid).run(grid.cell(TOP_LEFT));
			new SwingBFSAnimation(canvas, grid).runAnimation(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}