package de.amr.mazes.demos.maze;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.maze.algorithms.KruskalMST;
import de.amr.mazes.demos.grid.GridSampleApp;
import de.amr.mazes.demos.swing.rendering.BFSAnimation;

public class KruskalMSTApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new KruskalMSTApp());
	}

	public KruskalMSTApp() {
		super("Kruskal-MST Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new KruskalMST(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}