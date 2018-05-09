package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.util.GridUtils.log;

import java.util.stream.IntStream;

import de.amr.easy.grid.curves.HilbertLCurve;
import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class HilbertLCurveApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveApp());
	}

	public HilbertLCurveApp() {
		super(512, 512, 256, Topologies.TOP4);
		setAppName("Hilbert Curve (L-system)"); 
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			int startCell = grid.cell(BOTTOM_LEFT);
			HilbertLCurve hilbertCurve = new HilbertLCurve(log(2, grid.numCols()));
			hilbertCurve.traverse(grid, startCell, this::addEdge);
			SwingBFSAnimation bfs = new SwingBFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.run(startCell);
			sleep(1000);
		});
	}
}