package de.amr.demos.grid.curves;

import static de.amr.demos.grid.curves.CurveUtil.walkCurve;
import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.maze.misc.MazeUtils.log;

import java.util.stream.Stream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.iterators.curves.HilbertCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

/**
 * Creates Hilbert curves of different sizes and shows an animation of the creation and
 * BFS-traversal of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class HilbertCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HilbertCurveApp());
	}

	private HilbertCurveApp() {
		super("Hilbert Curve", 512, 512, 256);
	}

	private HilbertCurve createCurve(GridPosition start, int depth) {
		switch (start) {
		case TOP_RIGHT:
			return new HilbertCurve(depth, N, E, S, W);
		case TOP_LEFT:
			return new HilbertCurve(depth, N, W, S, E);
		case BOTTOM_RIGHT:
			return new HilbertCurve(depth, E, S, W, N);
		case BOTTOM_LEFT:
			return new HilbertCurve(depth, W, S, E, N);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void run() {
		Stream.of(TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT).forEach(startPosition -> {
			for (int cellSize = 256; cellSize >= 2; cellSize /= 2) {
				setCellSize(cellSize);
				setDelay(cellSize > 16 ? 5 : 2);
				int depth = log(2, getWidth() / cellSize);
				HilbertCurve hilbert = createCurve(startPosition, depth);
				walkCurve(grid, hilbert, grid.cell(startPosition), this::updateTitle);
				BFSAnimation bfs = new BFSAnimation(canvas, grid);
				bfs.setDistancesVisible(false);
				bfs.runAnimation(grid.cell(startPosition));
				sleep(1000);
			}
		});
	}
}