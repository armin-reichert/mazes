package de.amr.demos.maze.swingapp.view;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JSlider;

import de.amr.demos.maze.swingapp.app.CreateAllMazesAction;
import de.amr.demos.maze.swingapp.app.CreateSingleMazeAction;
import de.amr.demos.maze.swingapp.app.MazeDemoApp;
import de.amr.demos.maze.swingapp.app.StopTaskAction;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.maze.misc.MazeUtils;
import de.amr.easy.util.GridUtils;

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
		setTitle("Maze Generation Algorithms");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(new JMenuBar());
		getJMenuBar().add(algorithmMenu = new AlgorithmMenu(app.model, this));
		getJMenuBar().add(pathFinderMenu = new PathFinderMenu());
		getJMenuBar().add(optionMenu = new OptionMenu(app));

		controlPanel = new ControlPanel();

		/*@formatter:off*/
		controlPanel.getResolutionSelector().setModel(new DefaultComboBoxModel<>(
			Arrays.stream(app.model.getGridCellSizes())
				.mapToObj(cellSize -> {
					Dimension dim = MazeUtils.maxGridDimensionForDisplay(cellSize);
					return String.format("%d cells (%d x %d @ %d)", 
							dim.width * dim.height, dim.width, dim.height, cellSize);
				})
				.toArray(String[]::new))
		);
		/*@formatter:on*/

		controlPanel.getAlgorithmLabel().setText(algorithmMenu.getSelectedAlgorithm().getDescription());

		controlPanel.getResolutionSelector()
				.setSelectedIndex(GridUtils.indexOf(app.model.getGridCellSize(), app.model.getGridCellSizes()));

		controlPanel.getResolutionSelector().addActionListener(e -> {
			JComboBox<?> selector = (JComboBox<?>) e.getSource();
			int cellSize = app.model.getGridCellSizes()[selector.getSelectedIndex()];
			app.model.setGridCellSize(cellSize);
			Dimension dim = MazeUtils.maxGridDimensionForDisplay(cellSize);
			app.model.setGrid(new ObservableGrid<>(dim.width, dim.height, UNVISITED));
			app.updateCanvas();
		});

		controlPanel.getPassageThicknessSlider().setValue(app.model.getPassageThicknessPct());
		controlPanel.getPassageThicknessSlider().addChangeListener(e -> {
			JSlider slider = (JSlider) e.getSource();
			if (!slider.getValueIsAdjusting()) {
				app.setGridPassageThickness(slider.getValue());
			}
		});

		controlPanel.getDelaySlider().setValue(app.model.getDelay());
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
