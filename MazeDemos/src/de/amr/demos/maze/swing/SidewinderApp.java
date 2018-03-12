package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.Sidewinder;

public class SidewinderApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SidewinderApp());
	}

	public SidewinderApp() {
		super("Armin's algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new Sidewinder(grid).run(null);
			new SwingBFSAnimation(canvas, grid).runAnimation(grid.cell(TOP_LEFT));
			sleep(1000);
			clear();
		});
		System.exit(0);
	}
}
