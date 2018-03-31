package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import de.amr.demos.grid.swing.core.SwingBFSAnimation;
import de.amr.demos.grid.swing.core.SwingGridSampleApp;
import de.amr.easy.maze.alg.BoruvkaMST;

public class BoruvkaMSTApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new BoruvkaMSTApp());
	}

	public BoruvkaMSTApp() {
		super(32);
		setAppName("Boruvka-MST Maze");
	}

	@Override
	public void run() {
		new BoruvkaMST(grid).run(null);
		new SwingBFSAnimation(canvas, grid).run(grid.cell(TOP_LEFT));
		sleep(1000);
	}
}