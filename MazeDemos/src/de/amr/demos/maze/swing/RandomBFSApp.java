package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.ObservableDFSPathFinder;
import de.amr.easy.graph.traversal.HillClimbing;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingDFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.traversal.RandomBFS;

public class RandomBFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RandomBFSApp());
	}

	public RandomBFSApp() {
		super(128, Top4.get());
		setAppName("Randomized Bread-First-Traversal Maze");
	}

	@Override
	public void run() {
		renderer.fnPassageWidth = () -> renderer.getCellSize() * 90 / 100;
		IntStream.of(128, 64, 32, 16, 8, 4).forEach(cellSize -> {
			resizeGrid(cellSize);
			int source = grid.cell(TOP_LEFT), target = grid.cell(BOTTOM_RIGHT);
			new RandomBFS(grid).run(grid.cell(CENTER));
			// new SwingBFSAnimation(grid).runBFSAnimation(canvas, source);
			ObservableDFSPathFinder dfs = new HillClimbing(grid, source, target);
			new SwingDFSAnimation(grid).run(canvas, dfs, source, target);
			sleep(1000);
		});
		System.exit(0);
	}
}