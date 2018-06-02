package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.curves.CurveUtils.traverse;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.util.GraphUtils;

public class PeanoCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new PeanoCurveApp());
	}

	public PeanoCurveApp() {
		super(243 * 4, 243 * 4, 4, Top4.get());
		setAppName("Peano Curve");
	}

	@Override
	public void run() {
		IntStream.of(3, 9, 81, 243).forEach(n -> {
			resizeGrid(canvasSize.width / n);
			int startCell = grid.cell(BOTTOM_LEFT);
			PeanoCurve curve = new PeanoCurve(GraphUtils.log(3, n));
			traverse(curve, grid, startCell, this::addEdge);
			SwingBFSAnimation bfs = new SwingBFSAnimation(grid, canvas);
			bfs.setDistancesVisible(false);
			bfs.run(new BreadthFirstTraversal(grid), startCell, -1);
			sleep(1000);
		});
	}
}