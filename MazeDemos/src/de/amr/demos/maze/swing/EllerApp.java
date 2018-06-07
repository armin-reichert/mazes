package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.Eller;

public class EllerApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new EllerApp());
	}

	public EllerApp() {
		super(128, Top4.get());
		setAppName("Eller's Algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new Eller(grid).run(-1);
			new BreadthFirstTraversalAnimation(grid, new BreadthFirstTraversal(grid)).run(canvas, 0, -1);
			sleep(1000);
		});
		System.exit(0);
	}
}