package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircle;

public class WilsonExpandingCircleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonExpandingCircleApp());
	}

	public WilsonExpandingCircleApp() {
		super(64, Topologies.TOP4);
		setAppName("Wilson UST / Expanding Circle"); 
	}

	@Override
	public void run() {
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new WilsonUSTExpandingCircle(grid).run(grid.cell(CENTER));
			new SwingBFSAnimation(canvas, grid).run(grid.cell(CENTER));
			sleep(1000);
		});
		System.exit(0);
	}
}