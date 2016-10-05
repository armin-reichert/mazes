package de.amr.mazes.samples.maze;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.maze.algorithms.AldousBroderUST;
import de.amr.mazes.samples.grid.GridSampleApp;
import de.amr.mazes.swing.rendering.BFSAnimation;

public class AldousBroderUSTApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new AldousBroderUSTApp());
	}

	public AldousBroderUSTApp() {
		super("Aldous-Broder UST Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new AldousBroderUST(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}