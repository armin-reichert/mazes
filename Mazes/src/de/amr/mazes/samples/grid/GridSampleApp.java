package de.amr.mazes.samples.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.KeyStroke;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.impl.ObservableDataGrid;
import de.amr.easy.grid.rendering.DefaultGridRenderingModel;
import de.amr.easy.grid.rendering.GridCanvas;
import de.amr.easy.grid.rendering.GridRenderingModel;
import de.amr.easy.maze.misc.Utils;

/**
 * Base class for grid sample applications.
 * 
 * @author Armin Reichert
 */
public abstract class GridSampleApp implements Runnable {

	public static void launch(GridSampleApp app) {
		Utils.setLAF("Nimbus");
		EventQueue.invokeLater(app::showUI);
	}

	protected ObservableDataGrid<TraversalState> grid;
	protected int cellSize;
	protected String appName;
	protected JFrame window;
	protected GridCanvas<Integer, DefaultEdge<Integer>> canvas;
	protected JSlider delaySlider;

	protected GridSampleApp(String appName) {
		this(appName, 16);
	}

	protected GridSampleApp(String appName, int cellSize) {
		this.appName = appName;
		Dimension gridDimension = Utils.maxGridDimensionForDisplay(cellSize);
		init(gridDimension.width, gridDimension.height, cellSize);
	}

	protected GridSampleApp(String appName, int gridWidth, int gridHeight, int cellSize) {
		this.appName = appName;
		init(gridWidth, gridHeight, cellSize);
	}

	private void init(int gridWidth, int gridHeight, int cellSize) {
		grid = new ObservableDataGrid<>(gridWidth, gridHeight, UNVISITED);
		this.cellSize = cellSize;
	}

	protected void fitWindowSize(int windowWidth, int windowHeight, int cellSize) {
		grid = new ObservableDataGrid<>(windowWidth / cellSize, windowHeight / cellSize, UNVISITED);
		canvas.setGrid(grid);
		canvas.setRenderingModel(changeRenderingModel(cellSize));
		window.setTitle(composeTitle());
		window.pack();
	}

	private void showUI() {
		window = new JFrame();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setTitle(composeTitle());
		canvas = new GridCanvas<>(grid, changeRenderingModel(cellSize));
		canvas.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
		canvas.getActionMap().put("exit", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		canvas.setDelay(0);
		window.add(canvas, BorderLayout.CENTER);
		delaySlider = new JSlider(0, 50);
		delaySlider.setValue(canvas.getDelay());
		delaySlider.addChangeListener(event -> {
			if (!delaySlider.getValueIsAdjusting())
				canvas.setDelay(delaySlider.getValue());
		});
		window.add(delaySlider, BorderLayout.SOUTH);
		window.setExtendedState(MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.pack();
		window.setVisible(true);
		new Thread(this).start();
	}

	protected void clear() {
		grid.removeEdges();
		grid.clear();
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

	private DefaultGridRenderingModel<Integer> renderingModel = new DefaultGridRenderingModel<Integer>() {

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

	protected GridRenderingModel<Integer> changeRenderingModel(int cellSize) {
		renderingModel.setCellSize(cellSize);
		return renderingModel;
	}
}