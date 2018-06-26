package de.amr.demos.maze.swing;

import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;

public class RecursiveDivisionApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveDivisionApp());
	}

	public RecursiveDivisionApp() {
		super(128);
		setAppName("Recursive Division Maze");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			OrthogonalMazeGenerator generator = new RecursiveDivision(getCanvas().getWidth() / cellSize,
					getCanvas().getHeight() / cellSize);
			setGrid(generator.getGrid());
			generator.createMaze(0, 0);
			sleep(1000);
			floodFill(getCanvas(), getGrid(), 0);
			sleep(1000);
		});
		System.exit(0);
	}
}