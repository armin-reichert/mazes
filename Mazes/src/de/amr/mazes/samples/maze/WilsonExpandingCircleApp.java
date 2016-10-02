package de.amr.mazes.samples.maze;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.Stream;

import de.amr.easy.maze.algorithms.wilson.WilsonUSTExpandingCircle;
import de.amr.mazes.samples.grid.GridSampleApp;
import de.amr.mazes.swing.rendering.BFSAnimation;

public class WilsonExpandingCircleApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonExpandingCircleApp());
	}

	public WilsonExpandingCircleApp() {
		super("Wilson UST / Expanding Circle");
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(CENTER);
		Stream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new WilsonUSTExpandingCircle(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}