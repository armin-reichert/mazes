package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.lang.String.format;
import static java.lang.System.out;

import de.amr.easy.util.StopWatch;

public class FullGridApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new FullGridApp());
	}

	public FullGridApp() {
		super("Full Grid", 2);
		grid.setDefaultContent(COMPLETED);
		grid.makeFullGrid();
	}

	@Override
	public void run() {
		setDelay(0);
		StopWatch watch = new StopWatch();
		watch.runAndMeasure(canvas::render);
		out.println(format("Grid rendering took %.3f seconds", watch.getSeconds()));
	}
}
