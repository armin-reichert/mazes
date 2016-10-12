package de.amr.mazes.demos.maze;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.maze.algorithms.HuntAndKill;
import de.amr.mazes.demos.grid.GridSampleApp;
import de.amr.mazes.demos.swing.rendering.BFSAnimation;

public class HuntAndKillApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new HuntAndKillApp());
	}

	public HuntAndKillApp() {
		super("Hunt And Kill");
		setFullscreen(true);
	}

	@Override
	public void run() {
		Integer startCell = grid.cell(TOP_LEFT);
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			fitWindowSize(window.getWidth(), window.getHeight(), cellSize);
			new HuntAndKill(grid).accept(startCell);
			new BFSAnimation(canvas, grid).runAnimation(startCell);
			sleep(3000);
			clear();
		});
		System.exit(0);
	}
}