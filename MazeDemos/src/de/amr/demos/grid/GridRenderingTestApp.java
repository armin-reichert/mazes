package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static java.lang.String.format;
import static java.lang.System.out;

import de.amr.easy.grid.impl.Top8;
import de.amr.easy.util.StopWatch;

public class GridRenderingTestApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new GridRenderingTestApp());
	}

	public GridRenderingTestApp() {
		super("Full Grid", 512,512,128);
		grid.setTopology(Top8.Top8);
		grid.setDefaultContent(COMPLETED);
		grid.fill();
	}

	@Override
	public void run() {
		setDelay(0);
		StopWatch watch = new StopWatch();
		watch.runAndMeasure(canvas::render);
		out.println(format("Grid rendering took %.3f seconds", watch.getSeconds()));
	}
}
