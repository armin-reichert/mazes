package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.EllerInsideOut;

public class EllerInsideOutApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new EllerInsideOutApp());
	}

	public EllerInsideOutApp() {
		super("Eller's Algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			new EllerInsideOut(grid).accept(null);
			new SwingBFSAnimation(canvas, grid).runAnimation(grid.cell(GridPosition.TOP_LEFT));
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}