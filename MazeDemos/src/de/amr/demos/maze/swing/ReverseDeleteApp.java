package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST;

public class ReverseDeleteApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new ReverseDeleteApp());
	}

	public ReverseDeleteApp() {
		super(128);
		setAppName("Reverse-Delete-MST Maze"); 
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32).forEach(cellSize -> {
			resizeGrid(cellSize);
			new ReverseDeleteMST(grid).run(-1);
			new SwingBFSAnimation(canvas, grid).run(grid.cell(TOP_LEFT));
			sleep(1000);
		});
		System.exit(0);
	}
}