package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.iterators.curves.Curves.walk;
import static de.amr.easy.maze.misc.MazeUtils.log;
import static java.util.Arrays.asList;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.api.Direction;
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

	private final EnumMap<GridPosition, List<Direction>> orientation = new EnumMap<>(GridPosition.class);

	public static void main(String[] args) {
		launch(new HilbertCurveApp());
	}

	private HilbertCurveApp() {
		super("Hilbert Curve", 512, 512, 256);
		orientation.put(TOP_RIGHT, asList(N, E, S, W));
		orientation.put(TOP_LEFT, asList(N, W, S, E));
		orientation.put(BOTTOM_RIGHT, asList(E, S, W, N));
		orientation.put(BOTTOM_LEFT, asList(W, S, E, N));
	}

	@Override
	public void run() {
		Stream.of(GridPosition.values()).forEach(startPos -> {
			IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				setCellSize(cellSize);
				setDelay(cellSize > 16 ? 5 : 2);
				int i = log(2, getWidth() / cellSize);
				HilbertCurve hilbert = new HilbertCurve(i, orientation.get(startPos));
				walk(hilbert, grid, grid.cell(startPos), this::updateTitle);
				BFSAnimation bfs = new BFSAnimation(canvas, grid);
				bfs.setDistancesVisible(false);
				bfs.runAnimation(grid.cell(startPos));
				sleep(1000);
			});
		});
	}
}