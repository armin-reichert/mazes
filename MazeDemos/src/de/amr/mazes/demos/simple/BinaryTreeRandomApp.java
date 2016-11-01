package de.amr.mazes.demos.simple;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.mazes.demos.grid.GridSampleApp;

public class BinaryTreeRandomApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new BinaryTreeRandomApp());
	}

	public BinaryTreeRandomApp() {
		super("Binary Tree Maze", 64);
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new BinaryTreeRandom(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}