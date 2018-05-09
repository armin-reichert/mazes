package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.ust.WilsonUSTHilbertCurve;

public class WilsonHilbertApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonHilbertApp());
	}

	public WilsonHilbertApp() {
		super(128, Topologies.TOP4);
		setAppName("Wilson UST / Hilbert Curve Maze");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new WilsonUSTHilbertCurve(grid).run(grid.cell(TOP_LEFT));
			new SwingBFSAnimation(canvas, grid).run(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}