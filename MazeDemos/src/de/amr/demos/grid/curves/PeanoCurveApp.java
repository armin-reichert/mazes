package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.curves.Curves.traverse;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.curves.PeanoCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

public class PeanoCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new PeanoCurveApp());
	}

	public PeanoCurveApp() {
		super("Peano Curve", 243 * 4, 243 * 4, 4);
	}

	@Override
	public void run() {
		IntStream.of(3, 9, 81, 243).forEach(n -> {
			setDelay(n < 9 ? 4 : 2);
			setCellSize(getWidth() / n);
			Integer start = grid.cell(BOTTOM_LEFT);
			traverse(new PeanoCurve(log(3, n)), grid, start, this::addEdge);
			BFSAnimation bfs = new BFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.runAnimation(start);
			sleep(1000);
		});
	}
}