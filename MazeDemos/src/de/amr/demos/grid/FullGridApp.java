package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.impl.Top8;
import de.amr.easy.util.StopWatch;

public class FullGridApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FullGridApp());
	}

	public FullGridApp() {
		super(512);
		setAppName("Full Grid");
	}

	@Override
	public void run() {
		setCanvasAnimation(false);
		StopWatch watch = new StopWatch();
		Stream.of(Top4.get(), Top8.get()).forEach(topology -> {
			IntStream.of(512, 256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				setCellSize(cellSize);
				getGrid().setDefaultVertex(COMPLETED);
				getGrid().fill();
				watch.measure(getCanvas()::drawGrid);
				System.out.println(String.format("Grid (%d cells @%d) rendered in %.2f seconds", getGrid().numVertices(),
						cellSize, watch.getSeconds()));
				sleep(2000);
			});
		});
		System.exit(0);
	}
}
