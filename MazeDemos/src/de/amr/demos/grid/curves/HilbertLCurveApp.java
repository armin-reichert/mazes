package de.amr.demos.grid.curves;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.iterators.curves.lsystem.HilbertLCurve;
import de.amr.easy.maze.misc.MazeUtils;

public class HilbertLCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveApp());
	}

	private int windowSize = 1024;

	public HilbertLCurveApp() {
		super("Hilbert Curve (L-system)", 4, 4, 256);
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			int depth = MazeUtils.log(2, windowSize / getCellSize());
			HilbertLCurve curve = new HilbertLCurve(depth);
			System.out.println(curve);
			CurveUtil.walkCurve(grid, curve, grid.cell(GridPosition.BOTTOM_LEFT), () -> {
			});
			sleep(2000);
		});
	}
}