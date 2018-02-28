package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.KruskalMST;

public class KruskalMSTApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new KruskalMSTApp());
	}

	public KruskalMSTApp() {
		super("Kruskal-MST Maze");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			new KruskalMST(grid).run(grid.cell(TOP_LEFT));
			new SwingBFSAnimation(canvas, grid).runAnimation(grid.cell(TOP_LEFT));
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}