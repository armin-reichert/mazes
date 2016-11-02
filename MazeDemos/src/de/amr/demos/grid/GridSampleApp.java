package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.maze.misc.MazeUtils.getScreenResolution;
import static java.lang.String.format;
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

	private final String appName;
	private int cellSize;

	protected ObservableGrid<TraversalState, Integer> grid;

	private boolean fullscreen;
	private Dimension screenRes = getScreenResolution();
	protected JFrame window;
	protected SwingGridCanvas canvas;
	protected JSlider delaySlider;

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

	private Action exitAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

	/**
	 * Creates a sample app using all available screen space of the used display and a cell size of
	 * 16.
	 */
	protected GridSampleApp(String appName) {
		this(appName, 16);
	}

	/**
	 * Creates a sample app using all available screen space of the used display.
	 */
	protected GridSampleApp(String appName, int cellSize) {
		this.appName = appName;
		this.cellSize = cellSize;
		int numCols = screenRes.width / cellSize;
		int numRows = (screenRes.height - 20) / cellSize; // subtract slider height
		grid = new ObservableGrid<>(numCols, numRows, UNVISITED);
	}

	/**
	 * Creates a sample app large enough to display a grid with given dimensions.
	 */
	protected GridSampleApp(String appName, int gridColCount, int gridRowCount, int cellSize) {
		this.appName = appName;
		this.cellSize = cellSize;
		grid = new ObservableGrid<>(gridColCount, gridRowCount, UNVISITED);
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public int getCellSize() {
		return cellSize;
	}

	/**
	 * Sets the cell size of the displayed grid. Can only be called after the window has been created.
	 * 
	 * @param cellSize
	 *          new grid cell size
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		grid = new ObservableGrid<>(window.getWidth() / cellSize, window.getHeight() / cellSize, UNVISITED);
		canvas.setGrid(grid);
		renderingModel.setCellSize(cellSize);
		canvas.setRenderingModel(renderingModel);
		window.setTitle(composeTitle());
		window.pack();
	}

	public void setDelay(int delay) {
		delaySlider.setValue(delay);
		canvas.setDelay(delay);
	}

	private void showUI() {
		window = new JFrame();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setTitle(composeTitle());
		renderingModel.setCellSize(cellSize);
		canvas = new SwingGridCanvas(grid, renderingModel);
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
			window.setUndecorated(true);
			window.setAlwaysOnTop(true);
			window.setSize(screenRes.width, screenRes.height);
		}
		window.pack();
		window.setVisible(true);

		// start real work in new thread
		new Thread(this).start();
	}

	protected void clear() {
		grid.removeEdges();
		grid.clearContent();
		canvas.resetRenderingModel();
		canvas.clear();
	}

	protected String composeTitle() {
		return format("%s [%d cols %d rows %d cells @%d px]", appName, grid.numCols(), grid.numRows(), grid.numCells(),
				cellSize);
	}

	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}