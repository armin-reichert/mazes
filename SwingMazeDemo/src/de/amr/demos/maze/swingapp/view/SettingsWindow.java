package de.amr.demos.maze.swingapp.view;

import static java.lang.String.format;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.action.CreateAllMazesAction;
import de.amr.demos.maze.swingapp.action.CreateSingleMazeAction;
import de.amr.demos.maze.swingapp.action.RunPathFinderAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.view.menu.GenerationAlgorithmMenu;
import de.amr.demos.maze.swingapp.view.menu.OptionMenu;
import de.amr.demos.maze.swingapp.view.menu.PathFinderMenu;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

/**
 * This view enables the user to select the maze generation and path finder algorithm and all the
 * other settings.
 * 
 * @author Armin Reichert
 */
public class SettingsWindow extends JFrame {

	private final ControlPanel controlPanel;
	private final GenerationAlgorithmMenu generationAlgorithmMenu;
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
		int selectedIndex = IntStream.range(0, sizes.length).filter(i -> sizes[i] == model.getGridCellSize()).findFirst()
				.orElse(-1);
		controlPanel.getComboGridResolution().setSelectedIndex(selectedIndex);

		controlPanel.getComboGridResolution().addActionListener(e -> {
			JComboBox<?> selector = (JComboBox<?>) e.getSource();
			int cellSize = model.getGridCellSizes()[selector.getSelectedIndex()];
			model.setGridCellSize(cellSize);
			int width = displayMode.getWidth() / cellSize;
			int height = displayMode.getHeight() / cellSize;
			model.setGrid(new ObservableGrid<>(width, height, Top4.get(), TraversalState.UNVISITED, false));
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
		controlPanel.getBtnFindPath().setAction(new RunPathFinderAction(app));
		controlPanel.getBtnStop().setAction(new StopTaskAction(app));

		getContentPane().add(controlPanel, BorderLayout.CENTER);

		setJMenuBar(new JMenuBar());

		generationAlgorithmMenu = new GenerationAlgorithmMenu(
				item -> controlPanel.getLblGenerationAlgorithm().setText(item.getText()));
		MazeDemoModel.findAlgorithm(IterativeDFS.class).ifPresent(alg -> {
			generationAlgorithmMenu.setSelectedAlgorithm(alg);
			controlPanel.getLblGenerationAlgorithm().setText(alg.getDescription());
		});
		getJMenuBar().add(generationAlgorithmMenu);

		pathFinderMenu = new PathFinderMenu(app, evt -> {
			JMenuItem item = (JMenuItem) evt.getSource();
			AlgorithmInfo algInfo = (AlgorithmInfo) item.getClientProperty("algorithm");
			getControlPanel().getBtnFindPath().setEnabled(algInfo != AlgorithmInfo.NONE);
		});
		pathFinderMenu.findItemByInfo(AlgorithmInfo.NONE).ifPresent(item -> {
			getControlPanel().getBtnFindPath().setEnabled(!item.isSelected());
		});
		getJMenuBar().add(pathFinderMenu);

		optionMenu = new OptionMenu(app);
		getJMenuBar().add(optionMenu);

	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public GenerationAlgorithmMenu getAlgorithmMenu() {
		return generationAlgorithmMenu;
	}

	public OptionMenu getOptionMenu() {
		return optionMenu;
	}

	public PathFinderMenu getPathFinderMenu() {
		return pathFinderMenu;
	}
}