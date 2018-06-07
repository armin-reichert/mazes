package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.CENTER;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircle;

public class WilsonExpandingCircleApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new WilsonExpandingCircleApp());
	}

	public WilsonExpandingCircleApp() {
		super(64, Top4.get());
		setAppName("Wilson UST / Expanding Circle");
	}

	@Override
	public void run() {
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new WilsonUSTExpandingCircle(grid).run(grid.cell(CENTER));
			new BreadthFirstTraversalAnimation(grid, new BreadthFirstTraversal(grid)).run(canvas, 0, -1);
			sleep(1000);
		});
		System.exit(0);
	}
}