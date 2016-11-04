package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.iterators.curves.Curves.traverse;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.lsystem.HilbertLCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

public class HilbertLCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertLCurveApp());
	}

	public HilbertLCurveApp() {
		super("Hilbert Curve (L-system)", 512, 512, 256);
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			Integer start = grid.cell(BOTTOM_LEFT);
			traverse(new HilbertLCurve(log(2, getWidth() / cellSize)), grid, start, this::addEdge);
			BFSAnimation bfs = new BFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.runAnimation(start);
			sleep(1000);
		});
	}
}