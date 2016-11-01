package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.RecursiveDFS;

public class RecursiveDFSApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDFSApp());
	}

	public RecursiveDFSApp() {
		super("Randomized-DFS Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(256, 128, 64, 32).forEach(cellSize -> {
			setCellSize(cellSize);
			new RecursiveDFS(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}