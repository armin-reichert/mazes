package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.curves.CurveUtils.traverse;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.util.GraphUtils;

public class HilbertLCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveApp());
	}

	public HilbertLCurveApp() {
		super(512, 512, 256, Top4.get());
		setAppName("Hilbert Curve (L-system)");
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			int startCell = grid.cell(BOTTOM_LEFT);
			HilbertLCurve hilbertCurve = new HilbertLCurve(GraphUtils.log(2, grid.numCols()));
			traverse(hilbertCurve, grid, startCell, this::addEdge);
			BreadthFirstTraversalAnimation<?> anim = new BreadthFirstTraversalAnimation<>(new BreadthFirstTraversal<>(grid));
			anim.setDistanceVisible(false);
			anim.run(canvas, startCell, -1);
			sleep(1000);
		});
	}
}