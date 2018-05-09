package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.RecursiveDFS;

public class RecursiveDFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDFSApp());
	}

	public RecursiveDFSApp() {
		super(256, Topologies.TOP4);
		setAppName("Recursive-DFS Maze"); 
	}

	@Override
	public void run() {
		IntStream.of(256, 128, 64, 32).forEach(cellSize -> {
			resizeGrid(cellSize);
			new RecursiveDFS(grid).run(grid.cell(TOP_LEFT));
			new SwingBFSAnimation(canvas, grid).run(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}