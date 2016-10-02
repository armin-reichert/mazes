package de.amr.mazes.samples.maze;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.Stream;

import de.amr.easy.maze.algorithms.BinaryTree;
import de.amr.mazes.samples.grid.GridSampleApp;
import de.amr.mazes.swing.rendering.BFSAnimation;

public class BinaryTreeApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new BinaryTreeApp());
	}

	public BinaryTreeApp() {
		super("Binary Tree Maze", 64);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		Stream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new BinaryTree(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}