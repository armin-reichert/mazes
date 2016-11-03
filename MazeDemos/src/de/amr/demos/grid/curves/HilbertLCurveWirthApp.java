package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.iterators.curves.Curves.walk;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.lsystem.HilbertLCurveWirth;

public class HilbertLCurveWirthApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveWirthApp());
	}

	public HilbertLCurveWirthApp() {
		super("Hilbert Curve (L-system, Wirth)", 512, 512, 256);
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			walk(new HilbertLCurveWirth(log(2, getWidth() / cellSize)), grid, grid.cell(TOP_RIGHT));
			sleep(1000);
		});
	}
}