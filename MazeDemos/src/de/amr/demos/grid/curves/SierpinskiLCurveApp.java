package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.Dir8;
import de.amr.easy.grid.curves.SierpinskiLCurve;

public class SierpinskiLCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new SierpinskiLCurveApp());
	}

	private static int CELL_SIZE = 4;
	
	public SierpinskiLCurveApp() {
		super("Hilbert Curve (L-system)", 127 * CELL_SIZE, 127 * CELL_SIZE, CELL_SIZE);
	}

	@Override
	public void run() {
		IntStream.of(CELL_SIZE).forEach(cellSize -> {
			setCellSize(cellSize);
			SierpinskiLCurve curve = new SierpinskiLCurve(6);
			System.out.println(curve);
			Integer current = grid.cell(1, 0);
			grid.set(current, COMPLETED);
			for (Dir8 dir : curve) {
				current = grid.cell(grid.col(current) + dir.dx, grid.row(current) + dir.dy);
				grid.set(current, COMPLETED);
			}
			sleep(1000);
		});
	}
}