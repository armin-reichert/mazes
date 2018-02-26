package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircle;

public class WilsonExpandingCircleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonExpandingCircleApp());
	}

	public WilsonExpandingCircleApp() {
		super("Wilson UST / Expanding Circle");
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(CENTER);
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			new WilsonUSTExpandingCircle(grid).run(startCell);
			new SwingBFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}