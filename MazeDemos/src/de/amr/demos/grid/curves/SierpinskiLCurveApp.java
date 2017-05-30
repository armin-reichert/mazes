package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.curves.SierpinskiLCurve;
import de.amr.easy.grid.impl.Top8;

public class SierpinskiLCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new SierpinskiLCurveApp());
	}

	private static int CELL_SIZE = 4;

	public SierpinskiLCurveApp() {
		super("Sierpinski Curve (L-system)", 127 * CELL_SIZE, 127 * CELL_SIZE, CELL_SIZE);
		grid.setTopology(Top8.INSTANCE);
	}

	@Override
	public void run() {
		IntStream.of(CELL_SIZE).forEach(cellSize -> {
			setCellSize(cellSize);
			SierpinskiLCurve curve = new SierpinskiLCurve(6);
			System.out.println(curve);
			Integer current = grid.cell(1, 0);
			grid.set(current, COMPLETED);
			for (int dir : curve) {
				current = grid.cell(grid.col(current) + grid.getTopology().dx(dir),
						grid.row(current) + grid.getTopology().dy(dir));
				grid.set(current, COMPLETED);
			}
			sleep(1000);
		});
	}
}