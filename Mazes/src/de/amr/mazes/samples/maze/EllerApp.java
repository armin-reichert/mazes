package de.amr.mazes.samples.maze;

import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.algorithms.Eller;
import de.amr.mazes.samples.grid.GridSampleApp;
import de.amr.mazes.swing.rendering.BFSAnimation;

public class EllerApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new EllerApp());
	}

	public EllerApp() {
		super("Eller's Algorithm", 600, 360, 2);
		setFullscreen(true);
	}

	@Override
	public void run() {
		while (true) {
			new Eller(grid).accept(null);
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(GridPosition.TOP_LEFT));
			sleep(1000);
			clear();
		}
	}
}
