package de.amr.mazes.demos.simple;

import java.util.stream.IntStream;

import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.algorithms.EllerInsideOut;
import de.amr.mazes.demos.grid.GridSampleApp;
import de.amr.mazes.demos.swing.rendering.BFSAnimation;

public class EllerInsideOutApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new EllerInsideOutApp());
	}

	public EllerInsideOutApp() {
		super("Eller's Algorithm", 64);
		setFullscreen(true);
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new EllerInsideOut(grid).accept(null);
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(GridPosition.TOP_LEFT));
			sleep(1000);
			clear();
		});
	}
}