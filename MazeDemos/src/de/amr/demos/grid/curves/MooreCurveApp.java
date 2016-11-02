package de.amr.demos.grid.curves;

import static de.amr.demos.grid.curves.CurveUtil.walkCurve;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.lsystem.MooreLCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

/**
 * Creates Hilbert-Moore curves of different sizes and shows an animation of the creation and
 * BFS-traversal of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class MooreCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new MooreCurveApp(1024, 1024, 2));
	}

	public MooreCurveApp(int width, int height, int cellSize) {
		super("Hilbert-Moore Curve", width / cellSize, height / cellSize, cellSize);
	}

	@Override
	protected String composeTitle() {
		return super.composeTitle() + ", " + grid.edgeCount() + " edges";
	}

	@Override
	public void run() {
		IntStream.of(2, 4, 8, 16, 32, 64, 128, 256).forEach(n -> {
			setDelay(n < 16 ? 3 : 0);
			setCellSize(1024 / n);
			int startCol = n / 2, startRow = n - 1;
			Integer start = grid.cell(startCol, startRow);
			MooreLCurve moore = new MooreLCurve(log(2, n));
			walkCurve(grid, moore, start, () -> window.setTitle(composeTitle()));
			BFSAnimation bfs = new BFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.runAnimation(start);
		});
	}
}