package de.amr.demos.grid.curves;

import static de.amr.demos.grid.curves.CurveUtil.followCurve;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.IntStream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.MooreCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

/**
 * Creates Hilbert-Moore curves of different sizes and shows an animation of the creation and
 * BFS-traversal of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class MooreCurveApp extends GridSampleApp {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 1024;

	public static void main(String[] args) {
		launch(new MooreCurveApp());
	}

	private MooreCurveApp() {
		super("Hilbert-Moore Curve", WIDTH / 64, HEIGHT / 64, 64);
	}

	@Override
	protected String composeTitle() {
		return super.composeTitle() + ", " + grid.edgeCount() + " edges";
	}

	@Override
	public void run() {
		setDelay(0);
		IntStream.of(2, 4, 8, 16, 32, 64, 128, 256).forEach(n -> {
			MooreCurve moore = new MooreCurve(log(2, n));
			cellSize = WIDTH / n;
			fitWindowSize(WIDTH, HEIGHT, cellSize);
			int startCol = n / 2, startRow = n - 1;
			// System.out.println("n=" + n + ",start at col:" + startRow + ",row:" + startRow);
			Integer startCell = grid.cell(startCol, startRow);
			followCurve(grid, moore, startCell, () -> window.setTitle(composeTitle()));
			BFSAnimation bfs = new BFSAnimation(canvas, grid);
			bfs.setDistancesVisible(false);
			bfs.runAnimation(startCell);
		});
	}
}