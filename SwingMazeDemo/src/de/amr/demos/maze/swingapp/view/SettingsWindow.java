package de.amr.demos.maze.swingapp.view;

import static java.lang.String.format;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import de.amr.demos.maze.swingapp.view.menu.SolverMenu;

/**
 * This view enables the user to select the maze generation and path finder algorithm and all the
 * other settings.
 * 
 * @author Armin Reichert
 */
public class SettingsWindow extends JFrame {

	private final ControlPanel controlPanel;
	private final GeneratorMenu generatorMenu;
	private final SolverMenu solverMenu;
	private final OptionMenu optionMenu;

	public SettingsWindow(MazeDemoApp app) {
		final MazeDemoModel model = app.model;
		setTitle("Mazes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controlPanel = new ControlPanel();
		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(
				Arrays.stream(model.getGridCellSizes()).mapToObj(cellSize -> {
					Dimension screenSize = MazeDemoApp.getScreenSize();
					int numCols = screenSize.width / cellSize;
					int numRows = screenSize.height / cellSize;
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
		controlPanel.getBtnFindPath().setAction(app.actionRunMazeSolver);
		controlPanel.getBtnStop().setAction(app.actionStopTask);

		getContentPane().add(controlPanel, BorderLayout.CENTER);

		// Menus
		setJMenuBar(new JMenuBar());
		generatorMenu = new GeneratorMenu(app);
		getJMenuBar().add(generatorMenu);
		solverMenu = new SolverMenu(app);
		getJMenuBar().add(solverMenu);
		optionMenu = new OptionMenu(app);
		getJMenuBar().add(optionMenu);
	}

	public void setGeneratorName(String name) {
		controlPanel.getLblGenerationAlgorithm().setText(name);
	}

	public void setSolverName(String name) {
		controlPanel.getLblSolver().setText(name);
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

	public SolverMenu getSolverMenu() {
		return solverMenu;
	}
}