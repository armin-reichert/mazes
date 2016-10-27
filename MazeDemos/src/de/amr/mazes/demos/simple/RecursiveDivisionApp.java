package de.amr.mazes.demos.simple;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.mazes.demos.grid.GridSampleApp;
import de.amr.mazes.demos.swing.rendering.BFSAnimation;

public class RecursiveDivisionApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDivisionApp());
	}

	public RecursiveDivisionApp() {
		super("Recursive Division Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			grid.makeFullGrid().vertexStream().forEach(cell -> {
				grid.set(cell, COMPLETED);
			});
			new RecursiveDivision(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}