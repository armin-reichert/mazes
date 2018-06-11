package de.amr.demos.grid;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.impl.Top8;
import de.amr.easy.util.StopWatch;

public class FullGridApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FullGridApp());
	}

	public FullGridApp() {
		super(512, Top4.get());
		setAppName("Full Grid");
	}

	@Override
	public void run() {
		canvasAnimation.setEnabled(false);
		StopWatch watch = new StopWatch();
		Stream.of(Top4.get(), Top8.get()).forEach(topology -> {
			IntStream.of(512, 256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				resizeGrid(cellSize);
				grid.setDefaultContent(TraversalState.COMPLETED);
				grid.fill();
				watch.measure(canvas::drawGrid);
				System.out.println(String.format("Grid (%d cells @%d) rendered in %.2f seconds", grid.numCells(), cellSize,
						watch.getSeconds()));
				sleep(2000);
			});
		});
		System.exit(0);
	}
}
