package de.amr.demos.maze.swingapp.view;

import static java.lang.String.format;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSlider;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.view.menu.GeneratorMenu;
import de.amr.demos.maze.swingapp.view.menu.OptionMenu;
import de.amr.demos.maze.swingapp.view.menu.PathFinderMenu;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

/**
 * This view enables the user to select the maze generation and path finder algorithm and all the
 * other settings.
 * 
 * @author Armin Reichert
 */
public class SettingsWindow extends JFrame {

	private final ControlPanel controlPanel;
	private final GeneratorMenu generatorMenu;
	private final PathFinderMenu pathFinderMenu;
	private final OptionMenu optionMenu;

	public SettingsWindow(MazeDemoApp app) {
		final MazeDemoModel model = app.model;
		setTitle("Maze Generation Algorithms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controlPanel = new ControlPanel();
		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(
				Arrays.stream(model.getGridCellSizes()).mapToObj(cellSize -> {
					DisplayMode displayMode = MazeDemoApp.getDisplayMode();
					int numCols = displayMode.getWidth() / cellSize;
					int numRows = displayMode.getHeight() / cellSize;
					return format("%d cells (%d cols x %d rows, cell size %d)", numCols * numRows, numCols, numRows, cellSize);
				}).toArray(String[]::new));
		controlPanel.getComboGridResolution().setModel(comboModel);

		int[] sizes = model.getGridCellSizes();
		int selectedIndex = IntStream.range(0, sizes.length).filter(i -> sizes[i] == model.getGridCellSize()).findFirst()
				.orElse(-1);
		controlPanel.getComboGridResolution().setSelectedIndex(selectedIndex);

		controlPanel.getComboGridResolution().setAction(app.actionChangeGridResolution);

		controlPanel.getSliderPassageWidth().setValue(model.getPassageWidthPercentage());
		controlPanel.getSliderPassageWidth().addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				app.setGridPassageThickness(slider.getValue());
			}
		});

		controlPanel.getSliderDelay().setValue(model.getDelay());
		controlPanel.getSliderDelay().addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				app.setDelay(slider.getValue());
			}
		});

		controlPanel.getBtnCreateMaze().setAction(app.actionCreateMaze);
		controlPanel.getBtnCreateAllMazes().setAction(app.actionCreateAllMazes);
		controlPanel.getBtnFindPath().setAction(app.actionRunPathFinder);
		controlPanel.getBtnStop().setAction(app.actionStopTask);

		getContentPane().add(controlPanel, BorderLayout.CENTER);

		// Menus

		setJMenuBar(new JMenuBar());

		generatorMenu = new GeneratorMenu(item -> controlPanel.getLblGenerationAlgorithm().setText(item.getText()));
		MazeDemoModel.find(MazeDemoModel.GENERATOR_ALGORITHMS, IterativeDFS.class).ifPresent(alg -> {
			generatorMenu.setSelectedAlgorithm(alg);
			controlPanel.getLblGenerationAlgorithm().setText(alg.getDescription());
		});
		getJMenuBar().add(generatorMenu);

		pathFinderMenu = new PathFinderMenu(app);
		getJMenuBar().add(pathFinderMenu);

		optionMenu = new OptionMenu(app);
		getJMenuBar().add(optionMenu);
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public GeneratorMenu getGeneratorMenu() {
		return generatorMenu;
	}

	public OptionMenu getOptionMenu() {
		return optionMenu;
	}

	public PathFinderMenu getPathFinderMenu() {
		return pathFinderMenu;
	}
}