package de.amr.demos.maze.swingapp.view;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.lang.String.format;
import static java.util.stream.IntStream.range;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import de.amr.easy.grid.ui.swing.Display;

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

		setTitle("Maze Generation Algorithms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(new JMenuBar());
		getJMenuBar().add(algorithmMenu = new AlgorithmMenu(model, this));
		getJMenuBar().add(pathFinderMenu = new PathFinderMenu());
		getJMenuBar().add(optionMenu = new OptionMenu(app));

		controlPanel = new ControlPanel();

		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(
				Arrays.stream(model.getGridCellSizes()).mapToObj(cellSize -> {
					Dimension dim = Display.getScreenResolution();
					dim.width /= cellSize;
					dim.height /= cellSize;
					return format("%d cells (%d x %d @ %d)", dim.width * dim.height, dim.width, dim.height, cellSize);
				}).toArray(String[]::new));
		controlPanel.getResolutionSelector().setModel(comboModel);

		controlPanel.getAlgorithmLabel().setText(algorithmMenu.getSelectedAlgorithm().getDescription());

		int[] sizes = model.getGridCellSizes();
		int selectedIndex = range(0, sizes.length).filter(i -> sizes[i] == model.getGridCellSize()).findFirst().orElse(-1);
		controlPanel.getResolutionSelector().setSelectedIndex(selectedIndex);

		controlPanel.getResolutionSelector().addActionListener(e -> {
			JComboBox<?> selector = (JComboBox<?>) e.getSource();
			int cellSize = model.getGridCellSizes()[selector.getSelectedIndex()];
			model.setGridCellSize(cellSize);
			Dimension dim = Display.getScreenResolution();
			dim.width /= cellSize;
			dim.height /= cellSize;
			model.setGrid(new ObservableGrid<>(dim.width, dim.height, Top4.get(), UNVISITED, false));
			app.updateCanvas();
		});

		controlPanel.getPassageThicknessSlider().setValue(model.getPassageThicknessPct());
		controlPanel.getPassageThicknessSlider().addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				app.setGridPassageThickness(slider.getValue());
			}
		});

		controlPanel.getDelaySlider().setValue(model.getDelay());
		controlPanel.getDelaySlider().addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				app.setDelay(slider.getValue());
			}
		});

		controlPanel.getBtnCreateMaze().setAction(new CreateSingleMazeAction(app));
		controlPanel.getBtnCreateAllMazes().setAction(new CreateAllMazesAction(app));
		controlPanel.getBtnStop().setAction(new StopTaskAction(app));

		getContentPane().add(controlPanel, BorderLayout.CENTER);
		getContentPane().setPreferredSize(new Dimension(400, 200));
		pack();
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