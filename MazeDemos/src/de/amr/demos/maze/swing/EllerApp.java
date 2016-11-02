package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.Eller;

public class EllerApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new EllerApp());
	}

	public EllerApp() {
		super("Eller's Algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			new Eller(grid).accept(null);
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(GridPosition.TOP_LEFT));
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}