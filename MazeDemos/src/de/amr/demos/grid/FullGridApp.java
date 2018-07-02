package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.impl.Top8;

public class FullGridApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FullGridApp());
	}

	public FullGridApp() {
		super(1024, 1024, 512);
		setAppName("Full Grid");
	}

	@Override
	public void run() {
		setCanvasAnimation(false);
		Stream.of(Top8.get(), Top4.get()).forEach(topology -> {
			IntStream.of(512, 256, 128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
				setCellSize(cellSize);
				setGridTopology(topology);
				getGrid().setDefaultVertexLabel(COMPLETED);
				getGrid().fill();
				watch.measure(getCanvas()::drawGrid);
				System.out.println(String.format("Grid (%d cells @%d) rendered in %.2f seconds", getGrid().numVertices(),
						cellSize, watch.getSeconds()));
				sleep(1000);
			});
		});
		System.exit(0);
	}
}
