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
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new WilsonUSTExpandingCircle(grid).run(grid.cell(CENTER));
			new SwingBFSAnimation(canvas, grid).runAnimation(grid.cell(CENTER));
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}