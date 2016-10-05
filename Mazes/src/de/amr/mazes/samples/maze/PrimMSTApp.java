package de.amr.mazes.samples.maze;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.maze.algorithms.PrimMST;
import de.amr.mazes.samples.grid.GridSampleApp;
import de.amr.mazes.swing.rendering.BFSAnimation;

public class PrimMSTApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new PrimMSTApp());
	}

	public PrimMSTApp() {
		super("Prim-MST Maze");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new PrimMST(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}