package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import de.amr.easy.grid.curves.SierpinskiLCurve;
import de.amr.easy.grid.impl.Top8;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class SierpinskiLCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SierpinskiLCurveApp());
	}

	private static int CELL_SIZE = 8;

	public SierpinskiLCurveApp() {
		super(CELL_SIZE, Top8.get());
		setAppName("Sierpinski Curve (L-system)");
	}

	@Override
	public void run() {
		SierpinskiLCurve curve = new SierpinskiLCurve(6);
		System.out.println(curve);
		Integer current = grid.cell(1, 0);
		grid.set(current, COMPLETED);
		for (int dir : curve.dirs()) {
			current = grid.cell(grid.col(current) + grid.getTopology().dx(dir),
					grid.row(current) + grid.getTopology().dy(dir));
			grid.set(current, COMPLETED);
		}
	}
}