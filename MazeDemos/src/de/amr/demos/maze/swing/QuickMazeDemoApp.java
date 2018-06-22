package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Helper class for quick maze generation / flood-fill demo. Subclasses just implement a main method
 * like:
 * 
 * <pre>
 * 
 * public static void main(String[] args) {
 * 	QuickMazeDemoApp.launch("Aldous-Broder UST Maze", AldousBroderUST.class);
 * }
 * </pre>
 * 
 * @author Armin Reichert
 */
public class QuickMazeDemoApp extends SwingGridSampleApp {

	public static void launch(Class<? extends MazeGenerator> algorithmClass) {
		QuickMazeDemoApp app = new QuickMazeDemoApp(algorithmClass.getSimpleName(), algorithmClass);
		SwingGridSampleApp.launch(app);
	}

	private final Class<? extends MazeGenerator> algorithmClass;

	public QuickMazeDemoApp(String appName, Class<? extends MazeGenerator> algorithmClass) {
		super(128);
		this.algorithmClass = algorithmClass;
		setAppName(appName);
	}

	private MazeGenerator createAlgorithm() {
		try {
			return algorithmClass.getConstructor(OrthogonalGrid.class).newInstance(grid);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			resizeGrid(cellSize);
			createAlgorithm().run(0);
			BreadthFirstTraversalAnimation.floodFill(canvas, grid, 0);
			sleep(1000);
		});
		System.exit(0);
	}
}