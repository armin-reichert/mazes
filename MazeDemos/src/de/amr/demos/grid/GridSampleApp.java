package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.maze.misc.MazeUtils.getScreenResolution;
import static de.amr.easy.maze.misc.MazeUtils.setLAF;
import static java.awt.EventQueue.invokeLater;
import static java.lang.String.format;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.rendering.swing.SwingDefaultGridRenderingModel;
import de.amr.easy.grid.rendering.swing.SwingGridCanvas;

/**
 * Base class for grid sample applications.
 * 
 * @author Armin Reichert
 */
public abstract class GridSampleApp implements Runnable {

	public static void launch(GridSampleApp app) {
		setLAF("Nimbus");
		invokeLater(app::start);
	}

	private final String appName;
	private final int width;
	private final int height;
	private final boolean fullscreen;
	private int cellSize;
	protected ObservableGrid<TraversalState, Integer> grid;
	protected JFrame window;
	protected SwingGridCanvas canvas;

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

	public GridSampleApp(String appName, int width, int height, int cellSize) {
		this.appName = appName;
		this.fullscreen = false;
		this.width = width;
		this.height = height;
		this.cellSize = cellSize;
		int numCols = width / cellSize, numRows = height / cellSize;
		grid = new ObservableGrid<>(numCols, numRows, UNVISITED);
	}

	public GridSampleApp(String appName, int cellSize) {
		this.appName = appName;
		this.fullscreen = true;
		Dimension resolution = getScreenResolution();
		this.width = resolution.width;
		this.height = resolution.height;
		this.cellSize = cellSize;
		int numCols = width / cellSize, numRows = height / cellSize;
		grid = new ObservableGrid<>(numCols, numRows, UNVISITED);
	}

	public GridSampleApp(String appName) {
		this(appName, 16);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return format("%s [%d cols %d rows %d cells @%d px]", appName, grid.numCols(), grid.numRows(), grid.numCells(),
				cellSize);
	}

	public int getCellSize() {
		return cellSize;
	}

	/**
	 * Sets the cell size which resizes the grid to fit the window size. Can only be called after the
	 * window has been created.
	 * 
	 * @param cellSize
	 *          new grid cell size
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		grid = new ObservableGrid<>(width / cellSize, height / cellSize, UNVISITED);
		canvas.setGrid(grid);
		renderingModel.setCellSize(cellSize);
		canvas.setRenderingModel(renderingModel);
		window.setTitle(getTitle());
	}

	public void setDelay(int delay) {
		canvas.setDelay(delay);
	}

	private void start() {
		renderingModel.setCellSize(cellSize);

		canvas = new SwingGridCanvas(grid, renderingModel);
		canvas.setBackground(Color.BLACK);
		canvas.setDelay(0);
		canvas.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
		canvas.getActionMap().put("exit", exitAction);

		window = new JFrame();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setTitle(getTitle());
		window.setBackground(Color.BLACK);
		window.add(canvas, BorderLayout.CENTER);
		if (fullscreen) {
			window.setPreferredSize(new Dimension(width, height));
			window.setSize(window.getPreferredSize());
			window.setUndecorated(true);
			window.setAlwaysOnTop(true);
		} else {
			canvas.setPreferredSize(new Dimension(width, height));
			window.pack();
		}
		window.setVisible(true);

		// start real work in new thread
		new Thread(this).start();
	}

	public void clear() {
		grid.removeEdges();
		grid.clearContent();
		canvas.resetRenderingModel();
		canvas.clear();
	}

	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}