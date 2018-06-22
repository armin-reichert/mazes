package de.amr.demos.grid.curves;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;
import static de.amr.easy.grid.curves.CurveUtils.traverse;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;
import static de.amr.easy.util.GraphUtils.log;
import static java.util.Arrays.asList;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.curves.Curve;
import de.amr.easy.grid.curves.HilbertCurve;

/**
 * Creates Hilbert curves of different sizes and shows an animation of the creation and a flood-fill
 * of the underlying graph.
 * 
 * @author Armin Reichert
 */
public class HilbertCurveApp extends SwingGridSampleApp {

	private static final EnumMap<GridPosition, List<Integer>> ORIENTATION = new EnumMap<>(GridPosition.class);

	static {
		ORIENTATION.put(TOP_RIGHT, asList(N, E, S, W));
		ORIENTATION.put(TOP_LEFT, asList(N, W, S, E));
		ORIENTATION.put(BOTTOM_RIGHT, asList(E, S, W, N));
		ORIENTATION.put(BOTTOM_LEFT, asList(W, S, E, N));
	}

	public static void main(String[] args) {
		launch(new HilbertCurveApp());
	}

	private HilbertCurveApp() {
		super(512, 512, 256);
		setAppName("Hilbert Curve");
	}

	@Override
	public void run() {
		Stream.of(TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT).forEach(start -> {
			IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				resizeGrid(cellSize);
				List<Integer> dir = ORIENTATION.get(start);
				Curve hilbert = new HilbertCurve(log(2, grid.numCols()), dir.get(0), dir.get(1), dir.get(2), dir.get(3));
				traverse(hilbert, grid, grid.cell(start), this::addEdge);
				floodFill(canvas, grid, grid.cell(start), false);
				sleep(1000);
			});
		});
	}
}