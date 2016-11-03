package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.Curves;
import de.amr.easy.grid.iterators.curves.lsystem.HilbertLCurveWirth;
import de.amr.easy.maze.misc.MazeUtils;

public class HilbertLCurveWirthApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveWirthApp());
	}

	public HilbertLCurveWirthApp() {
		super("Hilbert Curve (L-system, Wirth book)", 512, 512, 256);
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			int depth = MazeUtils.log(2, getWidth() / getCellSize());
			HilbertLCurveWirth curve = new HilbertLCurveWirth(depth);
			Curves.walk(curve, grid, grid.cell(TOP_RIGHT));
			sleep(1000);
		});
	}
}