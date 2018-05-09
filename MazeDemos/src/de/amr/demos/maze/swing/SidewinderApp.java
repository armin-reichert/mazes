package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.Sidewinder;

public class SidewinderApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SidewinderApp());
	}

	public SidewinderApp() {
		super(128, Topologies.TOP4);
		setAppName("Armin's algorithm"); 
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new Sidewinder(grid).run(-1);
			new SwingBFSAnimation(canvas, grid).run(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}
