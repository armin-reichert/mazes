package de.amr.demos.maze.swingapp.view;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSlider;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.action.CreateAllMazesAction;
import de.amr.demos.maze.swingapp.action.CreateSingleMazeAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;

/**
 * This view enables the user to select the maze generation and path finder algorithm and all the
 * other settings.
 * 
 * @author Armin Reichert
 */
public class SettingsWindow extends JFrame {

	private final ControlPanel controlPanel;
	private final AlgorithmMenu algorithmMenu;
	private final PathFinderMenu pathFinderMenu;
	private final OptionMenu optionMenu;

	public SettingsWindow(MazeDemoApp app) {
		final MazeDemoModel model = app.model;
		final DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDisplayMode();

		setTitle("Maze Generation Algorithms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controlPanel = new ControlPanel();
		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(
				Arrays.stream(model.getGridCellSizes()).mapToObj(cellSize -> {
					int numCols = displayMode.getWidth() / cellSize;
					int numRows = displayMode.getHeight() / cellSize;
					return format("%d cells (%d cols x %d rows, cell size %d)", numCols * numRows, numCols, numRows, cellSize);
				}).toArray(String[]::new));
		controlPanel.getComboGridResolution().setModel(comboModel);

		int[] sizes = model.getGridCellSizes();
		int selectedIndex = range(0, sizes.length).filter(i -> sizes[i] == model.getGridCellSize()).findFirst().orElse(-1);
		controlPanel.getComboGridResolution().setSelectedIndex(selectedIndex);

		controlPanel.getComboGridResolution().addActionListener(e -> {
			JComboBox<?> selector = (JComboBox<?>) e.getSource();
			int cellSize = model.getGridCellSizes()[selector.getSelectedIndex()];
			model.setGridCellSize(cellSize);
			int width = displayMode.getWidth() / cellSize;
			int height = displayMode.getHeight() / cellSize;
			model.setGrid(new ObservableGrid<>(width, height, Top4.get(), UNVISITED, false));
			app.newCanvas();
		});

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

		controlPanel.getBtnCreateMaze().setAction(new CreateSingleMazeAction(app));
		controlPanel.getBtnCreateAllMazes().setAction(new CreateAllMazesAction(app));
		controlPanel.getBtnStop().setAction(new StopTaskAction(app));

		getContentPane().add(controlPanel, BorderLayout.CENTER);

		setJMenuBar(new JMenuBar());
		algorithmMenu = new AlgorithmMenu(model, item -> controlPanel.getLblGenerationAlgorithm().setText(item.getText()));
		getJMenuBar().add(algorithmMenu);
		pathFinderMenu = new PathFinderMenu();
		getJMenuBar().add(pathFinderMenu);
		optionMenu = new OptionMenu(app);
		getJMenuBar().add(optionMenu);

		controlPanel.getLblGenerationAlgorithm().setText(algorithmMenu.getSelectedAlgorithm().getDescription());
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public AlgorithmMenu getAlgorithmMenu() {
		return algorithmMenu;
	}

	public OptionMenu getOptionMenu() {
		return optionMenu;
	}

	public PathFinderMenu getPathFinderMenu() {
		return pathFinderMenu;
	}
}