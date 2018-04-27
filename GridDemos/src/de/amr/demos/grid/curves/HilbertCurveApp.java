package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static de.amr.easy.util.GridUtils.log;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.curves.HilbertCurve;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

/**
 * Creates Hilbert curves of different sizes and shows an animation of the creation and
 * BFS-traversal of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class HilbertCurveApp extends SwingGridSampleApp {

	private final Map<GridPosition, List<Integer>> orientation = new HashMap<>();

	public static void main(String[] args) {
		launch(new HilbertCurveApp());
	}

	private HilbertCurveApp() {
		super(512, 512, 256);
		orientation.put(TOP_RIGHT, asList(N, E, S, W));
		orientation.put(TOP_LEFT, asList(N, W, S, E));
		orientation.put(BOTTOM_RIGHT, asList(E, S, W, N));
		orientation.put(BOTTOM_LEFT, asList(W, S, E, N));
		setAppName("Hilbert Curve");
	}

	@Override
	public void run() {
		Stream.of(TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT).forEach(startPos -> {
			List<Integer> dirs = orientation.get(startPos);
			IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				resizeGrid(cellSize);
				HilbertCurve hilbert = new HilbertCurve(log(2, grid.numCols()), dirs.get(0), dirs.get(1), dirs.get(2),
						dirs.get(3));
				System.out.println(String.format("Grid cols:%d, rows:%d", grid.numCols(), grid.numRows()));
				System.out.println("Start position: " + startPos);
				System.out.println(hilbert);
				int startCell = grid.cell(startPos);
				hilbert.traverse(grid, startCell, this::addEdge);
				SwingBFSAnimation bfs = new SwingBFSAnimation(canvas, grid);
				bfs.setDistancesVisible(false);
				bfs.run(startCell);
				sleep(1000);
			});
		});
	}
}