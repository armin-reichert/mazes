package de.amr.mazes.demos.simple;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.grid.rendering.swing.BFSAnimation;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircle;
import de.amr.mazes.demos.grid.GridSampleApp;

public class WilsonExpandingCircleApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonExpandingCircleApp());
	}

	public WilsonExpandingCircleApp() {
		super("Wilson UST / Expanding Circle");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(CENTER);
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new WilsonUSTExpandingCircle(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}