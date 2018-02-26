package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.AldousBroderUST;

public class AldousBroderUSTApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new AldousBroderUSTApp());
	}

	public AldousBroderUSTApp() {
		super("Aldous-Broder UST Maze");
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4).forEach(cellSize -> {
			setCellSize(cellSize);
			new AldousBroderUST(grid).run(startCell);
			new SwingBFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}