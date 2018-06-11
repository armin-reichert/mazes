package de.amr.demos.maze.swing;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.maze.alg.MazeAlgorithm;

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

	public static void launch(String appName, Class<? extends MazeAlgorithm> algorithmClass) {
		SwingGridSampleApp.launch(new QuickMazeDemoApp(appName, algorithmClass));
	}

	private final Class<? extends MazeAlgorithm> algorithmClass;

	public QuickMazeDemoApp(String appName, Class<? extends MazeAlgorithm> algorithmClass) {
		super(128, Top4.get());
		this.algorithmClass = algorithmClass;
		setAppName(appName);
	}

	private MazeAlgorithm createAlgorithm() {
		try {
			return algorithmClass.getConstructor(Grid2D.class).newInstance(grid);
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