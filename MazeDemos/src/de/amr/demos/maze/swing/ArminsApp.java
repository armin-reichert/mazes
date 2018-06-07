package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.FloodFillAnimation;
import de.amr.easy.maze.alg.EllerInsideOut;

public class ArminsApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new ArminsApp());
	}

	public ArminsApp() {
		super(128, Top4.get());
		setAppName("Armin's algorithm");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			new EllerInsideOut(grid).run(-1);
			new FloodFillAnimation().run(canvas, grid, grid.cell(TOP_LEFT), true);
			sleep(1000);
		});
		System.exit(0);
	}
}
