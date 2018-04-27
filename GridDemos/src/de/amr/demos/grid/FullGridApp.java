package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.grid.impl.Topologies.TOP4;
import static de.amr.easy.grid.impl.Topologies.TOP8;
import static java.lang.String.format;
import static java.lang.System.out;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
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
		canvas.setDelay(0);
		canvas.stopListening();
		StopWatch watch = new StopWatch();
		Stream.of(TOP4, TOP8).forEach(topology -> {
			IntStream.of(512, 256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				resizeGrid(cellSize);
				grid.setTopology(topology);
				grid.setDefaultContent(COMPLETED);
				grid.fill();
				watch.runAndMeasure(canvas::drawGrid);
				out.println(
						format("Grid (%d cells @%d) rendered in %.2f seconds", grid.numCells(), cellSize, watch.getSeconds()));
				sleep(2000);
			});
		});
		System.exit(0);
	}
}
