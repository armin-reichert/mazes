package de.amr.demos.grid.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.graph.SwingGridSampleApp;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.core.MazeGenerator;

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
			MazeGenerator<OrthogonalGrid> generator = new RecursiveDivision(getCanvas().getWidth() / cellSize,
					getCanvas().getHeight() / cellSize);
			setGrid(generator.getGrid());
			generator.createMaze(0, 0);
			sleep(1000);
			floodfill();
			sleep(1000);
		});
		System.exit(0);
	}
}