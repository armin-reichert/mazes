package de.amr.demos.grid.maze.swing;

import static de.amr.graph.grid.ui.animation.BFSAnimation.floodFill;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Helper class for visualizing maze creation and flood-fill.
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

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				launch((Class<? extends MazeGenerator<OrthogonalGrid>>) ClassLoader.getSystemClassLoader()
						.loadClass(args[0]));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Usage: java de.amr.demos.grid.maze.swing.MazeDemoApp <maze generator class>");
		}
	}

	public static void launch(Class<? extends MazeGenerator<OrthogonalGrid>> generatorClass) {
		launch(new MazeDemoApp(generatorClass.getSimpleName(), generatorClass));
	}

	private final Class<? extends MazeGenerator<OrthogonalGrid>> generatorClass;

	public MazeDemoApp(String appName, Class<? extends MazeGenerator<OrthogonalGrid>> generatorClass) {
		super(128);
		this.generatorClass = generatorClass;
		setAppName(appName);
	}

	private MazeGenerator<OrthogonalGrid> createGenerator(int width, int height) {
		try {
			return generatorClass.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(width, height);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			MazeGenerator<OrthogonalGrid> generator = createGenerator(getCanvas().getWidth() / cellSize,
					getCanvas().getHeight() / cellSize);
			setGrid(generator.getGrid());
			generator.createMaze(0, 0);
			floodFill(getCanvas(), 0);
			sleep(1000);
		});
		System.exit(0);
	}
}