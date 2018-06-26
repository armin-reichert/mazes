package de.amr.demos.maze.swing;

import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;

/**
 * Helper class for visualizing maze creation and flood-fill.
 * 
 * <p>
 * Subclasses just implement a main method, e.g.:
 * 
 * <pre>
 * 
 * public static void main(String[] args) {
 * 	MazeDemoApp.launch(AldousBroderUST.class);
 * }
 * </pre>
 * 
 * @author Armin Reichert
 */
public class MazeDemoApp extends SwingGridSampleApp {

	public static void launch(Class<? extends ObservableMazeGenerator> generatorClass) {
		SwingGridSampleApp.launch(new MazeDemoApp(generatorClass.getSimpleName(), generatorClass));
	}

	private final Class<? extends ObservableMazeGenerator> generatorClass;

	public MazeDemoApp(String appName, Class<? extends ObservableMazeGenerator> generatorClass) {
		super(128);
		this.generatorClass = generatorClass;
		setAppName(appName);
	}

	private ObservableMazeGenerator createGenerator(int width, int height) {
		try {
			return generatorClass.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(width, height);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			ObservableMazeGenerator generator = createGenerator(getCanvas().getWidth() / cellSize,
					getCanvas().getHeight() / cellSize);
			setGrid(generator.getGrid());
			generator.createMaze(0, 0);
			floodFill(getCanvas(), getGrid(), 0);
			sleep(1000);
		});
		System.exit(0);
	}
}