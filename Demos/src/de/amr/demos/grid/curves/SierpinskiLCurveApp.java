package de.amr.demos.grid.curves;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.impl.curves.SierpinskiLCurve;

public class SierpinskiLCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SierpinskiLCurveApp());
	}

	private static int CELL_SIZE = 8;

	public SierpinskiLCurveApp() {
		super(CELL_SIZE);
		setAppName("Sierpinski Curve (L-system)");
	}

	@Override
	public void run() {
		// TODO make this work again
		Integer current = getGrid().cell(1, 0);
		getGrid().set(current, COMPLETED);
		for (int dir : new SierpinskiLCurve(6)) {
			current = getGrid().cell(getGrid().col(current) + getGrid().getTopology().dx(dir),
					getGrid().row(current) + getGrid().getTopology().dy(dir));
			getGrid().set(current, COMPLETED);
		}
	}
}