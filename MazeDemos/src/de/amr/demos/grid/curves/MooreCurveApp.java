package de.amr.demos.grid.curves;

import static de.amr.easy.grid.iterators.curves.Curves.walk;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.lsystem.MooreLCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

/**
 * Creates Moore curves of different sizes and shows an animation of the creation and BFS-traversal
 * of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class MooreCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new MooreCurveApp());
	}

	public MooreCurveApp() {
		super("Moore Curve", 512, 512, 256);
	}

	@Override
	public void run() {
		IntStream.of(2, 4, 8, 16, 32, 64, 128, 256).forEach(n -> {
			setDelay(n < 16 ? 3 : 0);
			setCellSize(getWidth() / n);
			int startCol = n / 2, startRow = n - 1;
			Integer start = grid.cell(startCol, startRow);
			MooreLCurve moore = new MooreLCurve(log(2, n));
			walk(moore, grid, start, this::updateTitle);
			BFSAnimation bfs = new BFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.runAnimation(start);
			sleep(1000);
		});
	}
}