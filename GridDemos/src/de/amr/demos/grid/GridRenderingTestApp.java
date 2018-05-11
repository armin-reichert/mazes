package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.lang.String.format;
import static java.lang.System.out;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.util.StopWatch;

public class GridRenderingTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new GridRenderingTestApp());
	}

	public GridRenderingTestApp() {
		super(256, Top4.get());
		setAppName("Full Grid");
	}

	@Override
	public void run() {
		canvas.setDelay(0);
		canvas.stopListening();
		StopWatch watch = new StopWatch();
		IntStream.of(256, 128, 64, 32, 16, 8, 4).forEach(cellSize -> {
			resizeGrid(cellSize);
			grid.setDefaultContent(COMPLETED);
			grid.fill();
			watch.runAndMeasure(canvas::drawGrid);
			out.println(format("Rendering grid with %d cells took %.3f seconds", grid.numCells(), watch.getSeconds()));
			sleep(2000);
		});
		System.exit(0);
	}
}
