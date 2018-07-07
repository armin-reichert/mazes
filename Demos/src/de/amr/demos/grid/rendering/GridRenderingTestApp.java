package de.amr.demos.grid.rendering;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;
import static java.lang.String.format;
import static java.lang.System.out;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;

public class GridRenderingTestApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new GridRenderingTestApp());
	}

	public GridRenderingTestApp() {
		super(256);
		setAppName("Grid Rendering Demo");
	}

	@Override
	public void run() {
		setCanvasAnimation(false);
		IntStream.of(256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			getGrid().setDefaultVertexLabel(COMPLETED);
			getGrid().fill();
			watch.measure(getCanvas()::drawGrid);
			out.println(
					format("Rendering getGrid() with %d cells took %.3f seconds", getGrid().numVertices(), watch.getSeconds()));
			floodFill(getCanvas(), getGrid(), getGrid().cell(CENTER));
			sleep(1000);
		});
		System.exit(0);
	}
}
