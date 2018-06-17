package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;
import static java.lang.String.format;
import static java.lang.System.out;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.util.StopWatch;

public class GridRenderingTestApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new GridRenderingTestApp());
	}

	public GridRenderingTestApp() {
		super(256, Top4.get(), SimpleEdge::new);
		setAppName("Full Grid");
		fullscreen = false;
	}

	@Override
	public void run() {
		canvasAnimation.setEnabled(false);
		StopWatch watch = new StopWatch();
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			grid.setDefaultVertex(COMPLETED);
			grid.fill();
			watch.measure(canvas::drawGrid);
			out.println(format("Rendering grid with %d cells took %.3f seconds", grid.numVertices(), watch.getSeconds()));
			floodFill(canvas, grid, grid.cell(CENTER));
			sleep(2000);
		});
		System.exit(0);
	}
}
