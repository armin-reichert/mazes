package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.Sidewinder;

public class SidewinderApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new SidewinderApp());
	}

	public SidewinderApp() {
		super(128, Top4.get());
		setAppName("Armin's algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new Sidewinder(grid).run(-1);
			new SwingBFSAnimation(grid, canvas).run(new BreadthFirstTraversal(grid), 0, -1);
			sleep(1000);
		});
		System.exit(0);
	}
}
