package de.amr.mazes.demos.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.KeyStroke;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.rendering.swing.SwingDefaultGridRenderingModel;
import de.amr.easy.grid.rendering.swing.SwingGridCanvas;
import de.amr.easy.grid.rendering.swing.SwingGridRenderingModel;
import de.amr.easy.maze.misc.MazeUtils;

/**
 * Base class for grid sample applications.
 * 
 * @author Armin Reichert
 */
public abstract class GridSampleApp implements Runnable {

	public static void launch(GridSampleApp app) {
		MazeUtils.setLAF("Nimbus");
		EventQueue.invokeLater(app::showUI);
	}

	protected ObservableGrid<TraversalState,Integer> grid;
	protected int cellSize;
	protected String appName;
	protected JFrame window;
	protected SwingGridCanvas canvas;
	protected JSlider delaySlider;
	private boolean fullscreen;

	protected GridSampleApp(String appName) {
		this(appName, 16);
	}

	protected GridSampleApp(String appName, int cellSize) {
		this.appName = appName;
		Dimension gridDimension = MazeUtils.maxGridDimensionForDisplay(cellSize);
		init(gridDimension.width, gridDimension.height, cellSize);
	}

	protected GridSampleApp(String appName, int gridWidth, int gridHeight, int cellSize) {
		this.appName = appName;
		init(gridWidth, gridHeight, cellSize);
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	private void init(int gridWidth, int gridHeight, int cellSize) {
		grid = new ObservableGrid<>(gridWidth, gridHeight, UNVISITED);
		this.cellSize = cellSize;
	}

	protected void fitWindowSize(int windowWidth, int windowHeight, int cellSize) {
		grid = new ObservableGrid<>(windowWidth / cellSize, windowHeight / cellSize, UNVISITED);
		canvas.setGrid(grid);
		canvas.setRenderingModel(changeRenderingModel(cellSize));
		window.setTitle(composeTitle());
		window.pack();
	}

	private Action exitAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

	private void showUI() {
		window = new JFrame();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setTitle(composeTitle());
		canvas = new SwingGridCanvas(grid, changeRenderingModel(cellSize));
		canvas.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
		canvas.getActionMap().put("exit", exitAction);
		canvas.setDelay(0);
		window.add(canvas, BorderLayout.CENTER);
		delaySlider = new JSlider(0, 50);
		delaySlider.setValue(canvas.getDelay());
		delaySlider.addChangeListener(event -> {
			if (!delaySlider.getValueIsAdjusting())
				canvas.setDelay(delaySlider.getValue());
		});
		window.add(delaySlider, BorderLayout.SOUTH);
		if (fullscreen) {
			window.setExtendedState(MAXIMIZED_BOTH);
			window.setUndecorated(true);
		}
		window.pack();
		window.setVisible(true);
		new Thread(this).start();
	}

	protected void clear() {
		grid.removeEdges();
		grid.clearContent();
		canvas.resetRenderingModel();
		canvas.clear();
	}

	protected String composeTitle() {
		return String.format("%s [%d x %d, %d cells]", appName, grid.numCols(), grid.numRows(),
				grid.numRows() * grid.numCols());
	}

	protected void setDelay(int delay) {
		delaySlider.setValue(delay);
		canvas.setDelay(delay);
	}

	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private SwingDefaultGridRenderingModel renderingModel = new SwingDefaultGridRenderingModel() {

		@Override
		public Color getCellBgColor(Integer cell) {
			switch (grid.get(cell)) {
			case VISITED:
				return Color.BLUE;
			case COMPLETED:
				return Color.WHITE;
			case UNVISITED:
				return getGridBgColor();
			default:
				return super.getCellBgColor(cell);
			}
		}
	};

	protected SwingGridRenderingModel changeRenderingModel(int cellSize) {
		renderingModel.setCellSize(cellSize);
		return renderingModel;
	}
}